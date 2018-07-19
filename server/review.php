<?php
$servername = "localhost";
$username = "username";
$password = "password";
$database = "database";

$conn = mysqli_connect($servername, $username, $password, $database);
if (mysqli_connect_errno()) die("error");
$conn->set_charset("utf8");

$db_mode = mysqli_real_escape_string($conn, $_GET["mode"]);
$restaurant_id = mysqli_real_escape_string($conn, $_GET["id"]);
$result = array();

if ($db_mode == "write") {
  $context = mysqli_real_escape_string($conn, $_POST["context"]);
  $star = mysqli_real_escape_string($conn, $_POST["star"]);
  if ($_POST['context'] == false || $_POST['star'] < 0 || $_POST['star'] > 5) {
    echo "data_error";
  }
  else {
    $sql = "SELECT COUNT(*) FROM `restaurant` WHERE id = '".$restaurant_id."';";
    $res = mysqli_query($conn, $sql);
    $count = mysqli_num_rows($res);
    if ($count) {
      $sql = "INSERT INTO `restaurant_review` (`restaurant_id`, `context`, `star`, `date`) VALUES ('".$restaurant_id."', '".$context."', '".$star."', '".date("Y-m-d")."');";
      $res = mysqli_query($conn, $sql);

      $sql = "SELECT AVG(star) FROM restaurant_review WHERE restaurant_id = '".$restaurant_id."';";
      $res = mysqli_query($conn, $sql);
      $row = mysqli_fetch_array($res);
      $avgStar = (string)$row[0];

      $sql = "UPDATE restaurant SET star = ".$avgStar." WHERE id = '".$restaurant_id."';";
      echo $sql;
      $res = mysqli_query($conn, $sql);
      echo "success";
    }
    else {
      echo "id_error";
    }
  }
  mysqli_close($conn);
}
else {
  $sql = "SELECT context, star, date FROM restaurant_review WHERE restaurant_id='".$restaurant_id."' ORDER BY id DESC;";
  $res = mysqli_query($conn, $sql);
  while ($row = mysqli_fetch_assoc($res))
    $result[] = $row;
  mysqli_close($conn);
  $json = json_encode($result, JSON_UNESCAPED_UNICODE);
  echo $json;
}
?>