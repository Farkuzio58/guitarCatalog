/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Controller que devuelve los datos de una guitarra.
 */

using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;

namespace GuitarCatalog.Controllers
{
    [ApiController]
    [Route("/get")]
    public class GuitarGetController : ControllerBase
    {
        [HttpGet]
        public ActionResult Get(int id)
        {
            try
            {
                string connectionString = "Server=localhost;Database=guitar_catalog;Uid=api;Pwd=1234;";
                Dictionary<string, object> guitar = new Dictionary<string, object>();
                using (MySqlConnection connection = new MySqlConnection(connectionString))
                {
                    connection.Open();
                    string sql = $"SELECT * FROM guitarra where id = {id}";
                    using (MySqlCommand command = new MySqlCommand(sql, connection))
                    {
                        using (MySqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                for (int i = 0; i < reader.FieldCount; i++)
                                {
                                    guitar[reader.GetName(i)] = reader.GetValue(i);
                                }

                            }
                        }
                    }
                }
                string json = JsonConvert.SerializeObject(guitar);
                return Ok(json);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }
    }
}
