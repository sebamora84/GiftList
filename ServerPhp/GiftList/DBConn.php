<?php
function GetDbConnection() {
	$servername = "mysql7.000webhost.com";
	$username = "a8471993_gift";
	$password = "giftlist248";
	$dbname = "a8471993_gift";
	
	$db = new PDO('mysql:host='.$servername.';dbname='.$dbname, $username, $password);
	$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
	return $db;
}
?>