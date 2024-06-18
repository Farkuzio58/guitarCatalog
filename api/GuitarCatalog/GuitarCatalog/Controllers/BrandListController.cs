/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Controller que devuelve el listado de marcas almacenadas en la base de datos.
 */

using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;

namespace GuitarCatalog.Controllers
{
    [ApiController]
    [Route("/brand")]
    public class BrandListController : ControllerBase
    {
        [HttpGet]
        public ActionResult Get()
        {
            string connectionString = "Server=localhost;Database=guitar_catalog;Uid=api;Pwd=1234;";
            try
            {
                List<string> brands = new List<string>();

                using (MySqlConnection connection = new MySqlConnection(connectionString))
                {
                    connection.Open();

                    string sql = "Select distinct marca from guitarra";
                    using (MySqlCommand command = new MySqlCommand(sql, connection))
                    {
                        using (MySqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {                                
                                    string brand  = reader.GetString(0);   

                                brands.Add(brand);
                            }
                        }
                    }
                }
                string json = JsonConvert.SerializeObject(brands);
                return Ok(json);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }
    }
}
