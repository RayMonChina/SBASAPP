using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.IO;
using System.Text;
using SBASApi.Models.Entity;
using SBASApi.Models.Request;
using SBASApi.Models.Response;
using SBASApi.DAL;
namespace SBASApi.BLL
{
    public class AndroidOperateService
    {
        SBASServiceDAL cbDal = new SBASServiceDAL();
        //注册手机
        public EquipmentRes Reg(EquipmentReg req)
        {
            var logRes = cbDal.Reg(req);
            return logRes;
        }
        //登录信息
        public LoginRes Login(LoginReq req)
        {
            var logRes = cbDal.Login(req);
            return logRes;
        }

        /// <summary>
        /// 获取联系人列表
        /// </summary>
        /// <param name="req"></param>
        /// <returns></returns>
        public ContactRes GetContacts(ContactReq req) {
            var res = new ContactRes();
            if (req == null) {
                res.isErrMsg = true;
                res.errMsg = "参数错误";
                return res;
            }
            res.ContactList = cbDal.GetContacts(req);
            return res;
        }
    }
}