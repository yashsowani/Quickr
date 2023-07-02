# Quickr File Sharing Application

Created a system that makes it easier to transfer a URL, text, or document from one device to another. There are several other solutions such as Google Drive and other cloud apps, but the main focus here is to eliminate the requirement for creating an account, downloading their software, or maintaining sync for particular files or folders. A Spring Web Application using REST that could be launched as a website and accessed by both PCs and smartphones would eliminate needless time wastage. This web based application will not require registration, just upload the file and immediately after it is uploaded, a QR code is generated a smartphone can scan that and a shortened URL is available that can be used on another PC to direct users to the resource's download landing page.

Following are some screenshots:

- Homepage

![Homepage](https://github.com/BabyJosemon/quickr/blob/main/homepage.png)

- Multiple File Uploading

![File Uploaded](https://github.com/BabyJosemon/quickr/blob/main/uploaded.png)

- QR Code and short URL generation

![QR Code Generation](https://github.com/BabyJosemon/quickr/blob/main/QRGenerate.png)

How to Compile and run the code?

- minikube start

- Create a docker hub account

- create a jar file of your project

- mvn clean compile install

Create the kubernetes resource
- kubectl apply -f kubernetes.yml


Exposes the external IP directly to any
program running on the host operating system.
- minikube tunnel

Automatically viewing the dashboard of Kubernetes
- minikube dashboard

Future changes in mind:

- Encoding of the original download URL is done for shortening purposes. This encoding is done right now using very straightforward and thus lacks the complexity of an actual cryptographic algorithm like AES. This would lead to a highly secure encryption and decryption methodology for our URL shortener.

- Implementing React along with Springboot as that would help with the page redirects and refreshes impacting performance.


