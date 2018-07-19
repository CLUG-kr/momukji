<?php
$servername = "localhost";
$username = "username";
$password = "password";
$database = "database";

$conn = mysqli_connect($servername, $username, $password, $database);
if(mysqli_connect_errno()) die('error');
$conn->set_charset("utf8");

$type = mysqli_real_escape_string($conn, $_GET["type"]);
$mode = mysqli_real_escape_string($conn, $_GET["mode"]);
$moreData = mysqli_real_escape_string($conn, $_GET["data"]);
$sql = null;
if ($mode == "favorite") {
  $sql = "SELECT id,icon,name,star,latitude,longitude FROM restaurant WHERE id IN ".$moreData." ORDER BY star DESC;";
} 
else if ($type != "all")
  $sql = "SELECT id,icon,name,star,latitude,longitude FROM restaurant WHERE type='".$type."' ORDER BY star DESC;";
else
  $sql = "SELECT id,icon,name,star,latitude,longitude FROM restaurant ORDER BY star DESC;";

$res=mysqli_query($conn, $sql);
$result=array();
while($row=mysqli_fetch_assoc($res))
  $result[]=$row;
mysqli_close($conn);

$json=json_encode($result, JSON_UNESCAPED_UNICODE);
echo $json;
?>