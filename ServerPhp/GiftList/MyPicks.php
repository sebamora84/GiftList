<?php
include 'DBConn.php';
echo GetMyPicks();

function GetMyPicks()
{
	$manager = new PicksManager();
	return $manager->GetUserPicks();
}

class PicksManager {
	function GetUserPicks() {  
		$userId = $_REQUEST['uid'];
		$giftPicks = $this->GetGiftsPicks($userId);
		return json_encode($giftPicks);
	}
	
	function GetGiftsPicks($userId) {
		$status=10;
		$db=GetDbConnection();	
		$query = $db->prepare("SELECT * FROM MyPicks WHERE UserId=:uid AND Status=:status");
		$query->bindParam(':uid', $userId);
		$query->bindParam(':status', $status);
		$query->execute();	
		$list=array();
		while ($row = $query->fetch()) {
			$pick = new Gift();
			$pick->Id = $row["Id"];
			$pick->Who = $row["Who"];
			$pick->Why  = $row["Why"];
			$pick->When = date_format(new DateTime($row["When"]), 'Y-m-d H:i:s');
			$pick->What =  $row["What"];
			array_push($list,$pick);
		}
		return $list;
	}
}

class Gift {
	public $Id = "";
	public $Who = "";
	public $Why  = "";
	public $When = "";
	public $What = "";
}    
?>