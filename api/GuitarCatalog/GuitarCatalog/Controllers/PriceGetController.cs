/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Controller que devuelve el precio más alto y más bajo de la base de datos en base a los filtros recibidos.
 */

using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;

namespace GuitarCatalog.Controllers
{
    [ApiController]
    [Route("/price")]
    public class PriceGetController : ControllerBase
    {
        [HttpGet]
        public ActionResult Get(string? favs, string? shape, string? brand, string? tremolo, string? strings, string? price)
        {
            string connectionString = "Server=localhost;Database=guitar_catalog;Uid=api;Pwd=1234;";
            try
            {
                List<int> prices = new List<int>();
                using (MySqlConnection connection = new MySqlConnection(connectionString))
                {
                    connection.Open();
                    string sql = "";
                    if (shape != null)
                    {
                        string shapes = "('Stratocaster', 'Telecaster', 'Double Cut', 'Single Cut')";
                        if (shapes.Contains(shape))
                            sql = "SELECT min(precio) as min, max(precio) as max from guitarra where forma = '" + shape + "'";
                        else
                            sql = "SELECT min(precio) as min, max(precio) as max from guitarra where forma not in " + shapes;
                    }
                    else
                        sql = "SELECT min(precio) as min, max(precio) as max from guitarra where forma <> 'notAShape'";
                    if (!string.IsNullOrEmpty(brand))
                        sql += " AND marca = '" + brand + "'";
                    if (!string.IsNullOrEmpty(tremolo))
                        sql += " AND tremolo = " + tremolo.Replace("yes", "1").Replace("no", "0");
                    if (!string.IsNullOrEmpty(strings))
                        sql += " AND nCuerdas = '" + strings + "'";
                    using (MySqlCommand command = new MySqlCommand(sql, connection))
                    {
                        using (MySqlDataReader reader = command.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                prices.Add(reader.IsDBNull(reader.GetOrdinal("min")) ? 0 : reader.GetInt32("min"));
                                prices.Add(reader.IsDBNull(reader.GetOrdinal("max")) ? 3000 : reader.GetInt32("max"));
                            }
                        }
                    }
                }
                string json = JsonConvert.SerializeObject(prices);
                return Ok(json);
            }
            catch (Exception ex)
            {
                return StatusCode(500, ex.Message);
            }
        }
    }
}


