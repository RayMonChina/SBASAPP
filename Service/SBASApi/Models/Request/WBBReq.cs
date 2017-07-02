using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models.Entity;
namespace SBASApi.Models.Request
{
    public class WBBReq:BaseRequest
    {
        public string LoginID { get; set; }//根据登录人ID获取表本信息
    }
}