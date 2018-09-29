package common;

import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo{
    private String username;
    private String status;

    private JSONObject singleUserInfoJSON;
    private String singleUserInfoString;

    private static ArrayList<UserInfo> userinfolist = new ArrayList <UserInfo>();

    /**
     * Construct UserInfo Instance.
     *
     * @param name
     * @param status
     * @param clientID
     */
    public UserInfo(String name, String status) {
        createUser(name, status);
    }
    
    public UserInfo(String name, String status, boolean addToList) {
        createUser(name, status);
        if(addToList==true)
        	userinfolist.add(this); 
    }
    
    /**
     * Construct UserInfo class by JsonString
     * @param s
     */
    public UserInfo(String s){
        singleUserInfoJSON = new JSONObject(s);

        this.username = singleUserInfoJSON.getString(Constants.USERNAME);
        this.status = singleUserInfoJSON.getString(Constants.STATUS);
        singleUserInfoString = singleUserInfoJSON.toString();
    }
    
    //Override
    public UserInfo(String s, boolean addToList){
        singleUserInfoJSON = new JSONObject(s);

        this.username = singleUserInfoJSON.getString(Constants.USERNAME);
        this.status = singleUserInfoJSON.getString(Constants.STATUS);
        singleUserInfoString = singleUserInfoJSON.toString();
        if(addToList==true)
        	userinfolist.add(this); 
    }


    // ------GET METHOD------------
    public String getUsername(){
        return this.username;
    }

    public String getStatus(){
        return this.status;
    }


    public void setUsername(String name){
        this.username = name;
        this.setJSON();
    }

    public void setStatus(String status){
        this.status = status;
        this.setJSON();
    }


    public void createUser(String name, String status){
        this.username = name;
        this.status = status;
        this.setJSON();
    }

    public void setJSON(){
        JsonUtil jsonutil = new JsonUtil();
		JSONObject data = new JSONObject();
		data.put(Constants.USERNAME, username);
		data.put(Constants.STATUS, status);

		singleUserInfoJSON = jsonutil.parse(Constants.USERINFO, data);
		singleUserInfoString = singleUserInfoJSON.toString();
    }

    /**
     * Get this UserInfo Object Json String
     * @return
     */
    public String toJsonString(){
        return singleUserInfoJSON.toString();
    }

    /**
     * Get this UserInfo Object Json
     * @return
     */
    public JSONObject toJSON(){
        return  singleUserInfoJSON;
    }

    /**
     * return String contains Userinfo List
     * @return
     */
    public static String getUserInfoListJsonString(){
        String s = "";
        for (UserInfo userinfoinstance : userinfolist){
            s+= userinfoinstance.toJsonString();
            s+= "$";
        }

        JSONObject sendJson = new JSONObject();
        sendJson.put(Constants.TYPE, Constants.ALLUSERINFO);
        sendJson.put(Constants.DATA, s);

        return sendJson.toString();
    }
    
    //get the static user info list
    public static ArrayList<UserInfo> getUserInfoList()
    {
    	return userinfolist;
    }
    
    //check the status of a user in the list
    public static String checkUserStatusInList(String queryUsername)
    {
    	for(UserInfo userinfo :userinfolist)
    	{
    		if(userinfo.getUsername().equals(queryUsername))
    			return userinfo.getStatus();
    	}
    	return null;
    }

    public static String[][] listToTableData()
    {
    	String[][] tableData = new String [userinfolist.size()][2];
    	for(int i=0;i<userinfolist.size();i++)
    	{
    		for(int j=0;j<2;j++)
    		{
    			switch(j)
    			{
    				case 0: tableData[i][j]=userinfolist.get(i).getUsername();
    				break;
    				case 1: tableData[i][j]=userinfolist.get(i).getStatus();
    				break;
    			}
    		}
    	}
    	return tableData;
    }
    
    public static ArrayList<UserInfo> subListWithSpecificStatus(String status)
    {
    	ArrayList<UserInfo> sublist = new ArrayList<UserInfo>();
    	for(UserInfo userinfo :userinfolist)
    	{
    		if(userinfo.getStatus().equals(status))
    			sublist.add(userinfo);
    	}
    	return sublist;
    }
    

 // ------------Process Json String fn Here---------------

     /**
      * update Current UserInfo Instance from JsonString
      * Ensure JsonString only contains single user info.
      * @param jsonString
      */
     public void updateUserInfoFromJsonString(String jsonString){
         JSONObject restoredJSON = new JSONObject(jsonString);
         if (restoredJSON.getString(Constants.USERNAME) != this.username){
             System.out.println("Username validation failed \n");
             return;
         }

         //this.username = restoredJSON.getString(Constants.USERNAME); //is Username allowed?
         this.status = restoredJSON.getString(Constants.USERNAME);
         this.singleUserInfoJSON = restoredJSON;
     }

     /**
      * update All Users Info from JsonString.
      * Only String from getUserInfoListJsonString()
      * @param jsonString
      */
     public static void updateAllUserInfoFromJsonString(String jsonString){
        JsonUtil ju = new JsonUtil();
    	 if (ju.getType(jsonString).equals(Constants.ALLUSERINFO)==false){
             System.out.println("Invalid String type \n");
             return;
         }
    	 JSONObject jo = new JSONObject(jsonString);
         String data = (String) jo.get(Constants.DATA);
        //System.out.println("data: "+data);
    	 String[] parts = data.split("\\$");
        // System.out.println("splited data part 1 :"+parts[0]);
         userinfolist.clear();
         for (String userStr : parts){
        	String singleUserInfoString = ju.getData(userStr).toString();
			@SuppressWarnings("unused")
			UserInfo temp = new UserInfo(singleUserInfoString ,true);
         }
       
         //another alternative
         /*for (String userStr : parts){
             for (UserInfo x : userinfolist){
                     x.updateUserInfoFromJsonString(userStr);
             }
         }*/
     }
     
     
     //update a user's status in the user info list
     public static void updataUserStatusInList(String username, String status) {
    	 for(UserInfo userinfo : UserInfo.getUserInfoList())
    	 {
    		 if(userinfo.getUsername().equals(username))
    			 {
    			 	userinfo.setStatus(status);
    			 	System.out.println("Status update complete");
    			 }
    	 }
     }
     
     public static void updataUserStatusInList(ArrayList<UserInfo>sublist, String status) {
    	 for(UserInfo sublistUserInfo : sublist)
    	 {
        	 for(UserInfo userinfo : UserInfo.getUserInfoList())
        	 {
        		 if(userinfo.getUsername().equals(sublistUserInfo.getUsername()))
        			 {
        			 	userinfo.setStatus(status);
        			 	System.out.println("Status update complete");
        			 }
        	 }
    	 }
     }


}
