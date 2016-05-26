
<?php
include 'DBConn.php';
   class Who {
	  public $Id = "";
      public $Name = "";
   }
   $listName = $_REQUEST['listName'];
   $whoList = GetList($listName);
   echo json_encode($whoList);
   
function GetList($listName) {
	   
	$db=GetDbConnection();	
	switch ($listName) {
	case "who":
		$query = $db->prepare("SELECT * FROM PossibleWho ORDER BY Name");
		break;
	case "what":
		$query = $db->prepare("SELECT * FROM PossibleWhat ORDER BY Name");
		break;
	case "why":
		$query = $db->prepare("SELECT * FROM PossibleWhy ORDER BY Name");
		break;
	}
	$query->execute();	
	$list=array();
	while ($row = $query->fetch()) {
	   
	   $pick = new Who();
	   $pick->Id = $row["Id"];
	   $pick->Name = $row["Name"];
	   array_push($list,$pick);
	}
	return $list;
}
?>
