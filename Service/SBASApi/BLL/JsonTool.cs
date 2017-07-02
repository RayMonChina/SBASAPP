using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Script.Serialization;
using System.IO;
using System.Text;
namespace SBASApi.BLL
{
    public static class JsonTool
    {
        public static List<T> JSONStringToList<T>(this string JsonStr)
        {
            List<T> objs = Newtonsoft.Json.JsonConvert.DeserializeObject<List<T>>(JsonStr);
            return objs;
        }

        public static T Deserialize<T>(string json)
        {
            T objs = Newtonsoft.Json.JsonConvert.DeserializeObject<T>(json); 
            return objs;
        }

        public static string Serialize(Object Input)
        {
            string retVal = Newtonsoft.Json.JsonConvert.SerializeObject(Input);
            return retVal;
        }

        //public static T Deserialize<T>(string json)
        //{
        //    T obj = Activator.CreateInstance<T>();
        //    using (MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(json)))
        //    {
        //        DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
        //        return (T)serializer.ReadObject(ms);
        //    }
        //}  
    }
}