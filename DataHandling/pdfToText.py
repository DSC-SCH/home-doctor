import pandas as pd
import os

from pdfminer.pdfinterp import PDFResourceManager, process_pdf
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfparser import PDFSyntaxError
from io import StringIO
from urllib.request import urlopen

os.chdir("./data/")
total = pd.read_csv('최종파일.csv', encoding='cp949')
total = total.dropna(subset=['용법용량'])


def read_pdf_file(pdfFile):
    rsrcmgr = PDFResourceManager()
    retstr = StringIO()
    laparams = LAParams()

    try:
        device = TextConverter(rsrcmgr, retstr, laparams=laparams)
        print(pdfFile)

        process_pdf(rsrcmgr, device, pdfFile)
        device.close()

        content = retstr.getvalue()
        retstr.close()
    except PDFSyntaxError as e:
        print(pdfFile)
        print(e)
        return None

    return content


def text_extration(col_, seperator):
    na = []

    for address in col_:
        contents = read_pdf_file(urlopen(address))
        print(contents)

        na.append(contents)

    na = ''.join(line.split())
    na2 = na.split(seperator)

    df = pd.DataFrame(list(na2), columns=[seperator]).drop([0])

    return df


effect = text_extration(total['효능효과'],'효능효과')
usage_capacity = text_extration(total['용법용량'], '용법용량')