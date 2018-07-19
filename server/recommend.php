<?php
$servername = "localhost";
$username = "username";
$password = "password";
$database = "database";

$korea = (int)$_GET["korea"];
$japan = (int)$_GET["japan"] + $korea;
$china = (int)$_GET["china"] + $japan;
$yang = (int)$_GET["yang"] + $china;
$snack = (int)$_GET["snack"] + $yang;
$chicken = (int)$_GET["chicken"] + $snack;
$pizza = (int)$_GET["pizza"] + $chicken;
$zok = (int)$_GET["zok"] + $pizza;
$dessert = (int)$_GET["dessert"] + $zok;
$fastfood = (int)$_GET["fastfood"] + $dessert;

$strType = "";
$ro = 0;
if ($fastfood) $ro = mt_rand(1, $fastfood);

if ($ro <= $korea) $strType = "korea";
else if ($ro <= $japan) $strType = "japan";
else if ($ro <= $china) $strType = "china";
else if ($ro <= $yang) $strType = "yang";
else if ($ro <= $snack) $strType = "snack";
else if ($ro <= $chicken) $strType = "chicken";
else if ($ro <= $pizza) $strType = "pizza";
else if ($ro <= $zok) $strType = "zok";
else if ($ro <= $dessert) $strType = "dessert";
else $strType = "fastfood";

$conn = mysqli_connect($servername, $username, $password, $database);
if(mysqli_connect_errno()) die('error');
$conn->set_charset("utf8");

$sql="SELECT MAX(star) FROM restaurant WHERE type ='".$strType."';";
$res=mysqli_query($conn, $sql);
$row=mysqli_fetch_array($res);
$resMax = (double)$row[0] - 1;

$sql="SELECT id,icon,name,star,latitude,longitude FROM restaurant WHERE type='".$strType."' AND star >= $resMax ORDER BY rand() LIMIT 3;";
$res=mysqli_query($conn, $sql);
$result=array();
while($row=mysqli_fetch_assoc($res))
  $result[]=$row;
mysqli_close($conn);

$json=json_encode($result, JSON_UNESCAPED_UNICODE);
echo $json;
?>