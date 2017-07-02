using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models.Entity;
namespace SBASApi.Models.Response
{
    [Serializable]
    public class ContactRes:BaseRes
    {
        public List<ContactListItem> ContactList { get; set; }
    }
}