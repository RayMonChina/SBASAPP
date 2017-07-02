using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models.Entity;
namespace SBASApi.Models.Response
{
    public class WUserItemRes:BaseRes
    {
        public List<WUserItem> userItems { get; set; }
    }
}