using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
namespace SBASApi.Models.Request
{
    public class ContactReq:BaseRequest
    {
        public string User_ID { get; set; }
    }
}