# JavaMailServer

## Description
This project is an implementation of an e-mail server that uses SMTP/IMAP protocols and allow registered users to send and receive emails.

## Goal
Get a more in depth understanding of the inner working of application layer protocols, and play around with sockets.

## Demonstration
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/demo.gif)

## Contexte
### a. Mail server
#### 1) Definition
A mail server (or email server) is a computer system that sends and receives email.

Mail servers send and receive email using standard email protocols. For example, the SMTP protocol sends messages and handles outgoing mail requests. The IMAP and POP3 protocols receive messages and are used to process incoming mail. When you log on to a mail server using a webmail interface or email client, these protocols handle all the connections behind the scenes.

#### 2) General Architecture
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/general_archi.png)

### b. SMTP protocol
#### 1) Definition
The Simple Mail Transfer Protocol is a communication protocol for electronic mail transmission. As an Internet standard, SMTP was first defined in 1982 by [RFC 821](https://tools.ietf.org/html/rfc821), and updated in 2008 by [RFC 5321](https://tools.ietf.org/html/rfc5321) to Extended SMTP additions, which is the protocol variety in widespread use today.

#### 2) Protocol architecture
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/smtp_archi.gif)

<img src="https://github.com/Noisy96/JavaMailServer/blob/master/images/smtp_agents.png" alt="drawing" width="500">
Figure: SMTP Agents

### c. IMAP protocol
#### 1) Definition
In computing, the Internet Message Access Protocol (IMAP) is an Internet standard protocol used by email clients to retrieve email messages from a mail server over a TCP/IP connection.[1] IMAP is defined by [RFC 3501](https://tools.ietf.org/html/rfc3501).

IMAP was designed with the goal of permitting complete management of an email box by multiple email clients, therefore clients generally leave messages on the server until the user explicitly deletes them. An IMAP server typically listens on port number 143. IMAP over SSL (IMAPS) is assigned the port number 993.

#### 2) Protocol architecture
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/imap_archi.png)

## Conception
### a. UML Diagrams
#### 1) State transition diagram
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/Diagramme%20d'etats%20transition.png)

#### 2) Sequence diagram
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/Diagramme%20de%20s%C3%A9quence%201.png)
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/Diagramme%20de%20s%C3%A9quence%202.png)

#### 3) Package diagram
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/diagramm%20de%20paquetage%20(server).png)
![](https://github.com/Noisy96/JavaMailServer/blob/master/images/diagramme%20de%20paquetage%20(client).png)

## Future enhancements
* Multiple clients support.
* Clients GUI.
* Sign up mencanism.
* more use cases handling (emails flags...).
* Add Exchange Agent (the agent responsible for forwarding emails to outer mail servers)

## References
[1] Mail Server Definition - https://techterms.com/definition/mail_server<br>
[2] Simple Mail Transfer Protocol - Wikipedia - https://en.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol<br>
[3] Internet Message Access Protocol - Wikipedia - https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol
