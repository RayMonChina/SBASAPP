using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using SBASApi.Models;
using SBASApi.Models.Request;
using SBASApi.Models.Entity;
using SBASApi.Models.Response;
using System.Text;
namespace SBASApi.DAL
{
    public class SBASServiceDAL : BaseDAL
    {
        public EquipmentRes Reg(EquipmentReg Ereg)
        {
            var retItem = new EquipmentRes();
            using (var context = WDbContext())
            {
                //首先查询数据库中是否存在
                string strSql = "select MEID,MECode,LoginID,States from MeterEquipment where MECode=@MECode";
                var retItems = context.Sql(strSql)
                    .Parameter("MECode", Ereg.MECode)
                    .QueryMany<EquipmentItem>();
                if (retItems == null||retItems.Count==0)
                {
                    //插入CODE记录
                    strSql = " INSERT INTO MeterEquipment (MECode) VALUES (@MECode)";
                    context.Sql(strSql)
                       .Parameter("MECode", Ereg.MECode).Execute();
                    retItem = new EquipmentRes();
                    retItem.isErrMsg = false;
                    retItem.errMsg = "";
                    return retItem;
                }
                else
                {
                    if (retItems.Count == 1)
                    {
                        if (retItems[0].LoginID == null || retItems[0].States != 0)
                        {
                            retItem.isErrMsg = true;
                            retItem.errMsg = "该设备暂不可用";
                            retItem.errMsgNo = 1;
                            return retItem;
                        }
                    }
                    else
                    {
                        retItems = retItems.Where(c => c.States == 0 && c.LoginID != null).ToList();
                    }
                }
                if (retItems == null || retItems.Count == 0)
                {
                    retItem.isErrMsg = true;
                    retItem.errMsg = "没有查询到数据";
                    retItem.errMsgNo = 1;
                }
                retItem.equipmentItems = retItems;
                return retItem;
            }
        }

        public LoginRes Login(LoginReq req)
        {
            using (var context = WDbContext())
            {
                string strSql = @"select User_ID as UserId,User_Code,User_Name from Base_UserInfo
                                  where User_Account = @account and User_Pwd = @pwd and DeleteMark=1";
                var retItem = context.Sql(strSql)
                    .Parameter("account", req.UserAccount)
                    .Parameter("pwd", req.Password)
                    .QuerySingle<LoginRes>();
                if (retItem == null)
                {
                    retItem = new LoginRes();
                    retItem.errMsg = "用户名或密码错误！";
                    retItem.isErrMsg = true;
                    retItem.errMsgNo = 1;
                }
                return retItem;
            }
        }

        /// <summary>
        /// 获取联系人列表
        /// </summary>
        /// <param name="req"></param>
        /// <returns></returns>
        public List<ContactListItem> GetContacts(ContactReq req)
        {
            var retList = new List<ContactListItem>();
            using (var context = WDbContext())
            {
                string strSql = @"select u.User_Name,u.User_ID,u.User_Code,i.PropertyInstance_Value as MobliePhoneNo,o.Organization_Name,o.Organization_Code
                                    from 
                                    Base_UserInfo u left join Base_StaffOrganize s on u.User_ID=s.User_ID
                                    left join Base_Organization o on o.Organization_ID=s.Organization_ID
                                    left join Base_AppendPropertyInstance i on u.User_ID=i.PropertyInstance_Key and i.Property_Control_ID='User_Telphone'
                                    where u.DeleteMark=1 and o.DeleteMark=1
                                     order by o.Organization_Code asc,u.User_Code";
                retList = context.Sql(strSql)
                    .QueryMany<ContactListItem>();
                return retList;

            }
        }


    }
}