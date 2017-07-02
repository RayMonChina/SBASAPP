using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models.Entity;
namespace SBASApi.Models.Request
{
    public class WPointReq:BaseRequest
    {
        public List<WPointItem> pointItems { get; set; }
    }
}