using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace TestAndroid.Models.Request
{
    public class DeviceReq:BaseRequest
    {
        public string deviceId { get; set; }
        public int isRegButton { get; set; }//0正常请求，通过注册按钮请求
    }
}