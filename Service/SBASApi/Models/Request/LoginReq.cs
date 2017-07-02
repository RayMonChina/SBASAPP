using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SBASApi.Models.Request
{
    public class LoginReq : BaseRequest
    {
        public string UserAccount { get; set; }
        public string Password { get; set; }
    }
}