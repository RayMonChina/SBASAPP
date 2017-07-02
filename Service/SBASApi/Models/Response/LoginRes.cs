using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SBASApi.Models.Response
{
    public class LoginRes:BaseRes
    {
        // //LOGINID,LOGINNAME,LOGINPASSWORD,USERNAME
        public string UserId { get; set; }
        public string User_Code { get; set; }
        public string User_Name { get; set; }
    }
}