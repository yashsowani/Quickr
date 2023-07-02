package com.example.Quickr.Controllers;

import com.example.Quickr.CustomExceptions.URLNotFoundException;
import com.example.Quickr.Entities.Document;
import com.example.Quickr.Entities.ErrorLogger;
import com.example.Quickr.Entities.Url;
import com.example.Quickr.Repository.DocumentRepository;
import com.example.Quickr.Service.*;
import com.google.zxing.WriterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.Option;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.*;


@SessionAttributes("sessionId")
@Controller
public class AppController {

    private static final String DOWNLOAD_LINK = "http://localhost:8000/downloadPage?sid=";
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private Environment env;

    @Autowired
    private UrlService urlService;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private FileDownloadService fileDownloadService;
    @Autowired
    private FileDeleteService fileDeleteService;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private AuditErrorService auditErrorService;

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    /*This method is to show homepage with current Uploads*/
    @GetMapping("/")
    public String viewHomePage(Model model, HttpSession session){
        String sessionId = session.getId();
        //List<Document> documentList = documentRepository.findAllBySession(sessionId);
        List<Document> documentList = fileDownloadService.downloadBySession(sessionId);
        if(documentList.size()>0){
            model.addAttribute("filesExist","true");
            model.addAttribute("documentList",documentList);
            model.addAttribute("sessionId",sessionId);
        }
        return "home";
    }

    /*This method is to show the download page*/
    @GetMapping("/downloadPage")
    public String showDownloadPage(@RequestParam("sid") String sessionId, Model model, HttpSession session){
        //String sessionId = session.getId();
        //List<Document> documentList = documentRepository.findAllBySession(sessionId);
        List<Document> documentList = fileDownloadService.downloadBySession(sessionId);
        model.addAttribute("documentList",documentList);
        return "download";
    }

    /*This method triggered when generating and downloading a seleced file with ID*/
    @GetMapping("/download")
    @ResponseBody
    public String viewDownloadPage(@Param("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        //Optional<Document> result =  documentRepository.findById(id);
        //Error Handling
        if(id==null){

        }
        Optional<Document> result =  fileDownloadService.downloadById(id);
        if(!result.isPresent()){
            throw new Exception("File not found with id: "+id);
        }
        Document document = result.get();
        httpServletResponse.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename="+document.getName();
        httpServletResponse.setHeader(headerKey,headerValue);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        servletOutputStream.write(document.getContent());
        servletOutputStream.close();
        return "download";
    }

    @GetMapping("/delete")
    public String deleteDocument(@Param("id") Long id, @RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer) throws Exception {
        if(id==null){

        }
        Optional<Document> result =  fileDeleteService.deleteDatabase(id);
        if(!result.isPresent()){
            throw new Exception("File not found with id: "+id);
        }
        return "redirect:" + referrer;
    }

    @GetMapping("/view")
    @ResponseBody
    public String viewViewPage(@Param("id") Long id, HttpServletResponse httpServletResponse) throws Exception {
        //Optional<Document> result =  documentRepository.findById(id);
        //Error Handling
        if(id==null){}

        Optional<Document> result =  fileDownloadService.downloadById(id);
        if(!result.isPresent()){
            throw new Exception("File not found with id: "+id);
        }
        Document document = result.get();
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(document.getName());
        httpServletResponse.setContentType(mimeType);
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        servletOutputStream.write(document.getContent());
        servletOutputStream.close();
        return "";
    }

    /*This method is called when uploading a file*/
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("document")MultipartFile[] multipartFiles, RedirectAttributes redirectAttributes, HttpSession session) throws IOException {
        String sessionId = session.getId();
        try {
            for (MultipartFile multipartFile : multipartFiles) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                Document document = new Document();
                document.setName(fileName);
                document.setContent(multipartFile.getBytes());
                document.setSize(multipartFile.getSize());
                document.setUploadDt(new Date());
                document.setActive(true);
                document.setSessionId(sessionId);
                fileUploadService.saveToDatabase(document);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        List<Document> documentList = fileDownloadService.downloadBySession(sessionId);
        redirectAttributes.addFlashAttribute("message","File Uploaded Successfully");//sent to thymeleaf
        redirectAttributes.addFlashAttribute("sessionID",sessionId);//send to QR
        return "redirect:/"; //redirect back to '/' handler
    }

    /*This method is for generating the QR*/
    @GetMapping("/generateQR")
    public String getQRCode(@ModelAttribute("sessionId") String sessionID, RedirectAttributes redirectAttributes) {//, @RequestParam(value = "text",defaultValue = "Knf") String text
        Date currentDate = new Date();
        String text = DOWNLOAD_LINK+sessionID;
        Url currentUrl = new Url();
        currentUrl.setLongUrl(text);
        currentUrl.setCreatedDate(currentDate);
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, Integer.parseInt(env.getProperty("linkExpirationTime")));
        currentDate = c.getTime();
        currentUrl.setExpiresDate(currentDate);
        String shortURL = urlService.convertToShortUrl(currentUrl);
        byte[] image = new byte[0];
        try {
            // Generate and Return Qr Code in Byte Array
            image = qrCodeService.getQRCode(text, 250, 250);

        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        // Convert Byte Array into Base64 Encode String
        String qrcode = Base64.getEncoder().encodeToString(image);
        redirectAttributes.addFlashAttribute("qrcode", qrcode);
        redirectAttributes.addFlashAttribute("shortUrl", shortURL);
        return "redirect:/";
    }

    /*This method triggered when short URL is clicked*/
    @GetMapping("/{shortId}")
    @ResponseBody
    public RedirectView openShortUrl(@PathVariable("shortId") String shortID, HttpServletResponse httpServletResponse){
        String url = urlService.getOriginalUrl(shortID);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }

    @ExceptionHandler(URLNotFoundException.class)
    public ModelAndView handleURLNotFoundException(HttpServletRequest request, Exception ex){
        Date currentDate = new Date();
        logger.error("Requested URL="+request.getRequestURL());
        logger.error("Exception Raised="+ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        ErrorLogger currentError = new ErrorLogger();
        currentError.setErrorCode("404");
        currentError.setErrorDesc(ex.getMessage());
        currentError.setTimestamp(currentDate);
        currentError.setRequestedURL(String.valueOf(request.getRequestURL()));
        auditErrorService.saveError(currentError);

        modelAndView.setViewName("error");
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleTransactionException(HttpServletRequest request, Exception ex){
        Date currentDate = new Date();
        logger.info("Transaction Error Occured: URL="+request.getRequestURL());
        logger.error("Requested URL="+request.getRequestURL());
        logger.error("Exception Raised="+ex);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.addObject("url", request.getRequestURL());

        ErrorLogger currentError = new ErrorLogger();
        currentError.setErrorCode("404");
        currentError.setErrorDesc(ex.getMessage());
        currentError.setTimestamp(currentDate);
        currentError.setRequestedURL(String.valueOf(request.getRequestURL()));
        auditErrorService.saveError(currentError);
        modelAndView.addObject("errorId", currentError.getId());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}


