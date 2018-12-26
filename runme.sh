export onixFile=/home/sftpuser/ONIX/pushmepress-ONIX.xml 
export imageUploadDir=/home/sftpuser/images/
export imgProcessDir=/home/springboot/images
./gradlew clean build && java -jar build/libs/onix-web-1.0.0.jar

