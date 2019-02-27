<?php

if(isset($_POST['submit'])){ 
    $fullname = $_POST['fullname'];
    $email = $_POST['email'];
    $phone = $_POST['phone'];
    $subject = $_POST['subject'];

    if(!empty($subject) && !empty($fullname) && !empty($email) && !empty($phone) && !empty($_POST['message'])) {
        $message = "Ime in Priimek: ".$fullname."\nEmail: ".$email."\nTelefon: ".$phone."\nSporocilo: ".$_POST['message'];
        $isSend = mail('luka.uranic12@gmail.com', $subject, $message);
        if($isSend) {
            header("Location: index.html?mailsend#send-message");
        }else {
            header("Location: index.html?error#send-message");
        }
    }else {
        header("Location: index.html?error#send-message");
    }
}else {
    header("Location: index.html?error#send-message");
}