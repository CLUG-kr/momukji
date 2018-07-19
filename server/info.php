<?php
$servername = "localhost";
$username = "username";
$password = "password";
$database = "database";

$conn = mysqli_connect($servername, $username, $password, $database);
if(mysqli_connect_errno()) die('error');
$conn->set_charset("utf8");

$id = mysqli_real_escape_string($conn, $_GET["id"]);
$sql = "SELECT name,type,image,star,latitude,longitude,description,phone,address,time,tag,menu FROM restaurant WHERE id='".$id."';";

$res=mysqli_query($conn, $sql);
$result=array();
while($row=mysqli_fetch_assoc($res))
  $result[]=$row;
mysqli_close($conn);

$json=json_encode($result, JSON_UNESCAPED_UNICODE);
echo $json;
?>