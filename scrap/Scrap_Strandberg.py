#
# Autor: Alejandro Valle Rodríguez
# Funcionalidad: Recopila datos de Strandberg
#

import requests
from bs4 import BeautifulSoup
import threading
import time
#from selenium import webdriver as uc #Mejor no usar este driver, undetected_chromedriver es mejor para este caso
from selenium.webdriver.common.by import By
#import requests
import undetected_chromedriver as uc
from fake_useragent import UserAgent

""" 
Comandos de windows para instalar las librerias necesarias:

pip install undetected-chromedriver
pip install fake-useragent
pip install disutils
pip install setuptools
pip install selenium
"""

class Worker(threading.Thread):
    """ Clase que hereda de thread para poder leer varias guitarras a la vez y aumentar velocidad de lectura """

    def __init__(self, url, resource, i, action):
        threading.Thread.__init__(self)
        self.url = url
        self.myHeaders = headers = {'User-Agent': "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36"}
        self.resource = resource
        self.i = i
        self.action = action

    def run(self):
        myHeaders = {'User-Agent': "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36"}
        htmlcode = requests.get(self.url, headers= myHeaders)
        soup = BeautifulSoup(htmlcode.text, 'html.parser')

        brand = 'Strandberg'
        model = soup.find('h1', class_='product_title entry-title').text
        try:
            externalId = soup.find('span', class_='text-muted').text.replace('SKU: ', '')
        except:
            externalId = model.replace(' ','')

        try:
            body = soup.find_all('div', class_='properties-data__caption')[1].find_all('li')[1].text.replace('body', 'Body')
        except:
            return  
        try:
            if(body.__contains__('Body:')):
                body = body[0:body.find("with")].replace("Body: ", '')
            else:
                body = body[0: body.find("Body")]
        except:
            pass
        body = body.replace('(6-string) ', '').replace('(7-string) ', '').replace('(8-string) ', '').replace('(7-string & 8-string)) ', '')
        neck = soup.find_all('div', class_='properties-data__caption')[3].find_all('li')[0].text.replace('Neck', 'neck')
        neck = neck[0: neck.find('neck')]
        fretboard = soup.find_all('div', class_='properties-data__caption')[3].find_all('li')[2].text
        if fretboard.__contains__('Fretboard Options'):
            fretboard = fretboard.replace('Fretboard Options: ', '')
        else:
            fretboard = fretboard[0: fretboard.find('Fretboard')]
        shape = 'Headless'
        lis = soup.find_all('div', class_='properties-data__caption')[5].find_all('li')
        configuration = lis[0].text.replace(' Pickup Configuration', '').replace(' ','')
        pickups = 'a'
        match configuration:
            case 'H–H':
                pickups = lis[1].text.replace('Neck: ', '') + '/' + lis[2].text.replace('Bridge: ', '')
            case 'H-S-H':
                pickups = lis[1].text.replace('Neck: ', '') + '/' + lis[2].text.replace('Middle: ', '') + '/' + lis[3].text.replace('Bridge: ', '')
            case 'S-S':
                pickups = lis[1].text.replace('Neck: ', '') + '/' + lis[2].text.replace('Bridge: ', '')
            case 'H-S-S':
                pickups = lis[1].text.replace('Neck: ', '') + '/' + lis[2].text.replace('Middle: ', '') + '/' + lis[3].text.replace('Bridge: ', '')
            case _:
                pickups = lis[1].text.replace('Neck: ', '') + '/' + lis[2].text.replace('Bridge: ', '')
                
        if configuration.__len__() > 7:
            configuration = "H-H"
        pickups = pickups.replace('\n', '')
        frets = soup.find_all('div', class_='properties-data__caption')[3].find_all('li')[4].text.split(' ')[0]
        if frets.__contains__('Fretboard') or frets.__contains__('”'):
            frets = soup.find_all('div', class_='properties-data__caption')[3].find_all('li')[5].text.split(' ')[0]
        if frets == None or frets == '' or frets[0] != '2':
            frets = soup.find_all('div', class_='properties-data__caption')[3].find_all('li')[4].find_next('span').text.split(' ')[1]
        frets = frets.replace(' ', '')
        nStrigs = soup.find('div', class_='product-categories').find_next('a').text.replace('-String Guitar', '')
        tremolo = False
        for a in soup.find('div', class_='product-categories').find_all('a'):
            if a.text == 'Tremolo':
                tremolo = True
        price = float(soup.find('span', class_='woocommerce-Price-amount amount').find_next('bdi').text.replace(',', '').replace('€',''))
        urlPicture = soup.find('img', class_='wp-post-image')['src']
        rotation: float = 0.0

        isReleased = soup.find('div', class_='qala-labels-label-id-389452')
        if isReleased is None:
            #Se comprueba que acción se desea realizar
            match self.action:
                case 'print': 
                    print("------------------- " + str(externalId) + " -------------------" + '\n' +                       
                    "item numero: " + str(self.i) + '\n' +
                    "Marca: " + str(brand) + '\n' +
                    "Modelo: " + str(model) + '\n' +
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
                    "Imagen: " + str(urlPicture) + '\n' +
                    "url: " + self.url + '\n' +
                    "----------------------------------------------------------" + '\n')
                case 'getList':
                    self.resource.append([1, externalId, brand, model, shape, body, neck, fretboard, None, configuration, pickups, frets, nStrigs, tremolo, price, urlPicture, self.url, rotation])   

class Strandberg():
    def __init__(self,idfuente:str, section:str, urlBase:str, url:str, urlSiguientePagina:str):
        self.fuente = idfuente
        self.url = url

    def test(self, numPag):
        """ Este método lee cada página disponible de Nissan y por cada anuncio se crea un hilo que lee los datos del coche 
        y los muestra por pantalla """
        URL = self.url
        driver = uc.Chrome()
        driver.get(URL)
        time.sleep(3) 
        soup = BeautifulSoup(driver.page_source, 'html.parser')
        driver.close()
        guitarCount = 0
        for li in soup.find('ul', class_='products columns-4 qesf-results-block qesf-results-block__columns--4 qesf-results-block__mobile-columns--2').find_all('li'):
            link = li.find_next('a')['href']
            Worker(link, None, guitarCount, 'print').start()
            guitarCount += 1
            if guitarCount % 10 == 0:
                time.sleep(4)
    
    def list(self):
        """ Este método lee cada página disponible de Nissan y por cada anuncio se crea un hilo que lee los datos del coche 
        y los añade a la lista, devolviendola una vez acabada la lectura """
        URL = "https://strandbergguitars.com/eu/product-category/guitar/"
        lista = []
        driver = uc.Chrome()
        driver.get(URL)
        time.sleep(3) 
        soup = BeautifulSoup(driver.page_source, 'html.parser')
        driver.close()
        guitarCount = 0
        guitars = soup.find('ul', class_='products columns-4 qesf-results-block qesf-results-block__columns--4 qesf-results-block__mobile-columns--2').find_all('li')
        for li in guitars:
            link = li.find_next('a')['href']
            worker = Worker(link, lista, guitarCount, 'getList')
            worker.start()
            guitarCount += 1
            if guitarCount % 10 == 0:
                time.sleep(4)
        while(worker.is_alive()):
            time.sleep(1)
        return lista
    
#PRUEBAS
#prueba = Strandberg(1, 'Guitarras Strandberg', 'https://strandbergguitars.com/eu/', 'https://strandbergguitars.com/eu/product-category/guitar/' , None)
#print(prueba.list().__len__())
#prueba.test(1)
#prueba.list()