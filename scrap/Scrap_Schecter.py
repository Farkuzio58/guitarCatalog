#
# Autor: Alejandro Valle Rodríguez
# Funcionalidad: Recopila datos de Schecter
#

import requests

class Schecter():
    def __init__(self, idfuente:str, section:str, urlBase:str, url:str, urlSiguientePagina:str):
        self.fuente = idfuente
        self.urlBase = urlBase
        self.url = url
        self.section = section

    def list(self):
        modoTest = False
        externalId = ''
        brand = 'Schecter'
        model = ''
        shape = ''
        body = ''
        neck = ''
        fretboard = ''
        configuration = 'H-H'
        pickups = ''
        frets = ''
        nStrigs = ''
        tremolo = ''
        url = ''

        response = requests.get(self.url)
        json = response.json()

        guitars = []

        i = 1
        data = json["items"]
        #print('se encontraron ' + str(data.__len__()) + ' guitarras')
        for item in data:
            externalId = item["internalid"]
            model:str = item["storedisplayname"]
            matrix_detail = item.get("matrixchilditems_detail", [])
            price = float(item["onlinecustomerprice_detail"]["onlinecustomerprice_formatted"].replace("$", "").replace(",", ""))
            url = self.urlBase + model.replace(' ', '-')
            if(model.__contains__('FR')):
                tremolo = True
            if model.__contains__('7'):
                nStrigs = '7'
            elif model.__contains__('8'):
                nStrigs = '8'
            elif model.__contains__('9'):
                nStrigs = '9'
            else:
                nStrigs = '6'
            if model.__contains__('FR'):
                tremolo = True
            else:
                tremolo = False
            if matrix_detail:
                neckPickup = matrix_detail[0]["custitem_neckpickup"]
                bridgePickup = matrix_detail[0]["custitem_bridgepickup"]
                pickups = neckPickup + ' / ' + bridgePickup
                body = matrix_detail[0]["custitem_bodymaterial"]
                neck = matrix_detail[0]["custitem_neckmaterial"]
                if(model.__contains__("Apocalypse")):
                    neck = neck.split(" ")[0]
                fretboard = neck
                frets = matrix_detail[0]["custitem_frets"].split(' ')[0]

            pictures = list()

            itemimages_detail = item.get("itemimages_detail", {})
                        
            if isinstance(itemimages_detail, dict) and len(itemimages_detail) == 1 and 'urls' in itemimages_detail:
                urls = itemimages_detail['urls']
                if isinstance(urls, list):
                    for img in urls:
                        if isinstance(img, dict) and 'url' in img:
                            pictures.append(color + '&&' + image_urls[0])
                            break
            else:
                for color, details in itemimages_detail.items():
                    if 'urls' in details:
                        image_urls = [img["url"] for img in details["urls"]]
                        if image_urls:
                            pictures.append(color + '&&' + image_urls[0])
                    elif isinstance(details, dict):
                        for sub_key, sub_details in details.items():
                            if isinstance(sub_details, dict):
                                for sub_sub_key, sub_sub_details in sub_details.items():
                                    if isinstance(sub_sub_details, dict) and 'urls' in sub_sub_details:
                                        image_urls = [img["url"] for img in sub_sub_details["urls"]]
                                        if image_urls:
                                            pictures.append(color + '&&' + image_urls[0])
                                            break 
                                        break  
                                    elif isinstance(sub_sub_details, dict):
                                        for sub_sub_sub_key, sub_sub_sub_details in sub_sub_details.items():
                                            if isinstance(sub_sub_sub_details, dict) and 'urls' in sub_sub_sub_details:
                                                image_urls = [img["url"] for img in sub_sub_sub_details["urls"]]
                                                if image_urls:
                                                    pictures.append(color + '&&' + image_urls[0])
                                                    break  
                                                break  
                                        break  
            if self.section == 'Demon':
                shape = 'Stratocaster'
            if self.section == 'Demon' and model.__contains__('Demon S'):
                shape = 'Double Cut'
            if self.section == 'Apocalypse':
                shape = 'Heavy'
            if self.section == 'Apocalypse' and model.__contains__('C-'):
                shape = 'Stratocaster'
            if self.section == 'Apocalypse' and model.__contains__('PT'):
                shape = 'Telecaster'
            if self.section == 'Apocalypse' and model.__contains__('Solo'):
                shape = 'Single Cut'
            if self.section == 'Reaper Elite':
                shape = 'Stratocaster'
            
            rotation: float = -90.0
            if "LH" in model and "Demon-8" not in model:
                rotation = 90.0

            for picture in pictures:
                guitars.append([1, externalId, brand, model + ' ' + picture.split('&&')[0], shape, body, neck, fretboard, None, configuration, pickups, frets, nStrigs, tremolo, price, picture.split('&&')[1], url, rotation])   
                if(modoTest):
                    print("------------------- " + str(externalId) + " -------------------" + '\n' +                       
                        "item numero: " + str(i) + '\n' +
                        "Marca: " + str(brand + '\n' +
                        "Modelo: " + str(model + ' ' + picture.split('&&')[0])) + '\n' +
                        "Forma: " + str(shape) + '\n' +
                        "Cuerpo: " + str(body) + '\n' +
                        "Mástil: " + str(neck) + '\n' +
                        "Diapasón: " + str(fretboard) + '\n' +
                        "Trastes: " + str(frets) + '\n' +
                        "Configuracion: " + str(configuration) + '\n' +
                        "Pastillas: " + str(pickups) + '\n' +
                        "Cuerdas: " + str(nStrigs) + '\n' +
                        'Tremolo: ' + str(tremolo) + '\n' +
                        "Precio: " + str(price) + '\n' +
                        "Imagen: " + str(picture.split('&&')[1]) + '\n' +
                        "Rotacion: " + str(rotation) + '\n' +
                        "url: " + url + '\n' +
                        "----------------------------------------------------------" + '\n')
            i += 1
        return guitars

#api_url = 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Fdemon&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc'  
#api_url = 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Fapocalypse&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc'
#api_url = 'https://www.schecterguitars.com/api/items?c=765602&commercecategoryurl=%2Fguitars%2Freaper-elite&country=US&currency=USD&fieldset=search&include=facets&language=en&limit=24&n=2&offset=0&pricelevel=5&sort=custitem1%3Aasc'
#guitarras = Schecter(None, 'Demon', 'https://www.schecterguitars.com/', api_url, None).list()