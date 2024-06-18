#
# Autor: Alejandro Valle Rodríguez
# Funcionalidad: Llama a la base de datos y por cada marca encontrada llama a su scrap correspondiente.
#

from Scrap_Strandberg import Strandberg
import mysql.connector
from Scrap_Schecter import Schecter
#"pip install mysql-connector-python"

conexion = mysql.connector.connect(
    host="localhost",
    user="scraper",
    password="1234",
    database="guitar_catalog"
)
lista = []

cursor = conexion.cursor()

sql_select = "SELECT * FROM marca"
cursor.execute(sql_select)

# Obtener los resultados de la consulta
lista = []
marcas = cursor.fetchall()
for marca in marcas:
    match marca[0]:
        case 1:
            strandberg = Strandberg(marca[0], marca[2], marca[3], marca[4], marca[5])
            lista = strandberg.list()
            #try:
            for guitar in lista:
                sql_insert = """
                    CALL InsertGuitarIfNotExists(
                        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                    )
                """
                datos_insert = (
                    guitar[1], guitar[2], guitar[3], guitar[4], guitar[5], guitar[6], 
                    guitar[7], guitar[8], guitar[9], guitar[10], guitar[11], guitar[12], 
                    guitar[13], guitar[14], guitar[15], guitar[16], guitar[17]
                )
                try:
                    cursor.execute(sql_insert, datos_insert)
                except:
                    pass
            conexion.commit()
            #except Exception as e:
            #print('No se pudieron añadir las guitarras a la base de datos\n')
        case 2:
            schecter = Schecter(marca[0], marca[2], marca[3], marca[4], marca[5])
            lista = schecter.list()
            #try:select
            for guitar in lista:
                sql_insert = """
                    CALL InsertGuitarIfNotExists(
                        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                    )
                """
                
                # Definir los datos a insertar, incluyendo el nuevo campo 'rotacion'
                datos_insert = (
                    guitar[1], guitar[2], guitar[3], guitar[4], guitar[5], guitar[6], 
                    guitar[7], guitar[8], guitar[9], guitar[10], guitar[11], guitar[12], 
                    guitar[13], guitar[14], guitar[15], guitar[16], guitar[17]
                )
                try:
                    cursor.execute(sql_insert, datos_insert)
                except:
                    pass
            conexion.commit()
            #except Exception as e:
            #print('No se pudieron añadir las guitarras a la base de datos\n')
        case 3:
            schecter = Schecter(marca[0], marca[2], marca[3], marca[4], marca[5])
            lista = schecter.list()
            #try:
            for guitar in lista:
                sql_insert = """
                    CALL InsertGuitarIfNotExists(
                        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                    )
                """                
                # Definir los datos a insertar, incluyendo el nuevo campo 'rotacion'
                datos_insert = (
                    guitar[1], guitar[2], guitar[3], guitar[4], guitar[5], guitar[6], 
                    guitar[7], guitar[8], guitar[9], guitar[10], guitar[11], guitar[12], 
                    guitar[13], guitar[14], guitar[15], guitar[16], guitar[17]
                )
                
                cursor.execute(sql_insert, datos_insert)
                
                    
            conexion.commit()
            #except Exception as e:
            #print('No se pudieron añadir las guitarras a la base de datos\n')
        case 4:
            schecter = Schecter(marca[0], marca[2], marca[3], marca[4], marca[5])
            lista = schecter.list()
            #try:
            for guitar in lista:
                sql_insert = """
                    CALL InsertGuitarIfNotExists(
                        %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s
                    )
                """                
                # Definir los datos a insertar, incluyendo el nuevo campo 'rotacion'
                datos_insert = (
                    guitar[1], guitar[2], guitar[3], guitar[4], guitar[5], guitar[6], 
                    guitar[7], guitar[8], guitar[9], guitar[10], guitar[11], guitar[12], 
                    guitar[13], guitar[14], guitar[15], guitar[16], guitar[17]
                )
                
                cursor.execute(sql_insert, datos_insert)
                    
            conexion.commit()
            print('Se han añadido las guitarras a la base de datos')
            #except Exception as e:
            #print('No se pudieron añadir las guitarras a la base de datos\n')
# Cerrar el cursor y la conexión
cursor.close()
conexion.close()