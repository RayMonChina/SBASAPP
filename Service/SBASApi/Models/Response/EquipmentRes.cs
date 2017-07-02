using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models.Entity;
namespace SBASApi.Models.Response
{
    public class EquipmentRes : BaseRes
    {
        public List<EquipmentItem> equipmentItems { get; set; }
    }
}