OSNetworkApplicationHook
========================
An open source network application hook in Java for OS X

######Idea
Hooks functions in network application and parses information from those hooks.

######Current
Currently opens and hooks Chrome. It then reads information from the send() call, parses out specified forms, and prints out their form value pairs.

######Problems
Doesn't currently work on SSL as the sent information is encrypted.

######Eventually
Add support for Firefox, Skype, and maybe more commonly used network applications.
