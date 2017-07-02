using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace SBASApi.Models.Entity
{
    [Serializable]
    public class ContactListItem
    {
        public string User_Name { get; set; }
        public string User_ID { get; set; }
        public string User_Code { get; set; }
        public string MobliePhoneNo { get; set; }
        public string Organization_Name { get; set; }
        public string Organization_Code { get; set; }
    }
}