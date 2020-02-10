import os
import pandas as pd
import logging

os.chdir('C:/Users/student/Desktop')

medical = pd.read_csv('./박성아/global/OpenData_ItemPermitDetail20200208.csv',encoding='cp949')
medical = medical[medical['취소상태']=='정상']

from pdfminer.pdfparser import PDFParser, PDFDocument
from pdfminer.pdfinterp import PDFResourceManager, process_pdf
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from io import StringIO
from io import open
from urllib.request import urlopen
import time

logging.propagate = False
logging.getLogger().setLevel(logging.ERROR)
#logger = logging.getLogger(__name__)

def read_pdf_file(pdfFile):
    try:
        rsrcmgr = PDFResourceManager()
        retstr = StringIO()
        laparams = LAParams()
        device = TextConverter(rsrcmgr, retstr, laparams=laparams)

        process_pdf(rsrcmgr, device, pdfFile)
        device.close()

        content = retstr.getvalue()
        retstr.close()

    except:
        content = '예외발생'

        return content


def text_extration(col_, seperator):
    na = []

    for address in col_:
        contents = read_pdf_file(urlopen(address))

        na.append(contents)

    na = ''.join(split())
    na2 = na.split(seperator)

    df = pd.DataFrame(list(na2), columns=[seperator]).drop([0])

    return df


effect = text_extration(total['효능효과'],'효능효과')
usage_capacity = text_extration(medical['용법용량'], '용법용량')


