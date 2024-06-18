/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Controller que devuelve el listado de guitarras de la base de datos en base a los filtros recibidos.
 */

using Microsoft.AspNetCore.Mvc;
using MySql.Data.MySqlClient;
using Newtonsoft.Json;

namespace GuitarCatalog.Controllers
{
    [ApiController]
    [Route("/list")]
    public class GuitarListController : ControllerBase
    {
        [HttpGet]
        public ActionResult Get(string? favs, string? shape, string? brand, string? tremolo, string? strings, string? price, string? patternToSearch)
        {
            string connectionString = "Server=localhost;Database=guitar_catalog;Uid=api;Pwd=1234;";
            try
            {
                List<Dictionary<string, object>> guitars = new List<Dictionary<string, object>>();
                using (MySqlConnection connection = new MySqlConnection(connectionString))
                {
                    connection.Open();
                    string sql;
                    if (patternToSearch != null)
                        sql = "SELECT * from guitarra WHERE CONCAT(marca, ' ', modelo) like '%" + patternToSearch + "%'";
                    else
                    {
                        if (favs != null)
                        {
                            sql = "SELECT * FROM guitarra where id in (" + favs + ")";
                        }
                        else if (shape != null)
                        {
                            string shapes = "('Stratocaster', 'Telecaster', 'Double Cut', 'Single Cut')";
                            if (shapes.Contains(shape))
                                sql = "SELECT * FROM guitarra where forma = '" + shape + "'";
                            else
                                sql = "SELECT * FROM guitarra where forma not in " + shapes;
                        }
                        else
                            sql = "SELECT * FROM guitarra where forma <> 'notAShape'";
                        if (!string.IsNullOrEmpty(brand))
                            sql += " AND marca = '" + brand + "'";
                        if (!string.IsNullOrEmpty(tremolo))
                            sql += " AND tremolo = " + tremolo.Replace("yes", "1").Replace("no", "0");
                        if (!string.IsNullOrEmpty(strings))
                            sql += " AND nCuerdas = '" + strings + "'";
                        if (!string.IsNullOrEmpty(price))
                            sql += " AND precio BETWEEN " + price;
                    }
                    using (MySqlCommand command = new MySqlCommand(sql, connection))
                    {
                        using (MySqlDataReader reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Dictionary<string, object> guitar = new Dictionary<string, object>();

                                for (int i = 0; i < reader.FieldCount; i++)
                                {
                                    guitar[reader.GetName(i)] = reader.GetValue(i);
                                }

                                guitars.Add(guitar);
                            }
                        }
                    }
                }
                string json = JsonConvert.SerializeObject(guitars);
                return Ok(json);
            }
            catch (Exception ex)
            {
                return StatusCode(500, $"Error: {ex.Message}");
            }
        }
    }
}
