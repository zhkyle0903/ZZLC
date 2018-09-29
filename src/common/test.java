package common;


public class test {

	public static void main(String[] args) {
		
		UserInfo test = new UserInfo("abc",Constants.INGAME,true);
		UserInfo test2 = new UserInfo("abccc",Constants.INGAME,true);
		System.out.println(UserInfo.checkUserStatusInList("abc"));
		System.out.println(UserInfo.checkUserStatusInList("acc"));
		System.out.println(test.getStatus());
		String listStr = UserInfo.getUserInfoListJsonString();
		System.out.println(listStr);
        JsonUtil ju = new JsonUtil();
    	System.out.println(ju.getType(listStr));
    	
		UserInfo.updateAllUserInfoFromJsonString(listStr);

		UserInfo test3 = new UserInfo("abcccccc",Constants.INGAME,true);
		System.out.println(UserInfo.checkUserStatusInList("abc"));
		System.out.println(UserInfo.checkUserStatusInList("abccc"));
		
		String[][] tableData = UserInfo.listToTableData();
    	for(int i=0;i<UserInfo.getUserInfoList().size();i++)
    	{
    		for(int j=0;j<2;j++)
    		{
    			switch(j)
    			{
    				case 0: System.out.println(tableData[i][j]);
    				break;
    				case 1: System.out.println(tableData[i][j]);
    				break;
    			}
    		}
    	}

		
	}

}
