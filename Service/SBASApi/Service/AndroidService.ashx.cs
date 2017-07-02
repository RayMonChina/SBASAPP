using System;
using System.Configuration;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Script.Serialization;
using SBASApi.BLL;
using SBASApi.Models.Request;
using SBASApi.Models.Response;
using SBASApi.Models.Entity;
namespace SBASApi.Service
{
    /// <summary>
    /// AndroidService 的摘要说明
    /// </summary>
    public class AndroidService : IHttpHandler
    {

        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/plain";
            var res = "";
            if (!string.IsNullOrEmpty(context.Request["type"]))
            {
                var reqType = context.Request["type"];
                var gson = context.Request["gson"];
                if (string.IsNullOrEmpty(gson))
                {
                    return;
                }
                if ("true".Equals(ConfigurationManager.AppSettings["showlog"]))
                {
                    SimpleLog.WriteLog(gson);
                }

                AndroidOperateService AService = new AndroidOperateService();
                Object objectRes = new BaseRes() { errMsgNo = 1, isErrMsg = true, errMsg = "不支持该操作" };
                switch (reqType)
                {
                    case "0"://注册
                        objectRes = AService.Reg(JsonTool.Deserialize<EquipmentReg>(gson));
                        break;
                    case "1"://登录
                        objectRes = AService.Login(JsonTool.Deserialize<LoginReq>(gson));
                        break;
                    case "2":
                        objectRes = AService.GetContacts(JsonTool.Deserialize<ContactReq>(gson));
                        break;
                    default:
                        break;
                }
                res = JsonTool.Serialize(objectRes);
            }
            if ("true".Equals(ConfigurationManager.AppSettings["showlog"]))
            {
                SimpleLog.WriteLog(res);
            }
            context.Response.Write(res);

        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}