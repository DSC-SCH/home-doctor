import pandas as pd
import os
from bs4 import BeautifulSoup
from selenium import webdriver

path = './chromedriver_win32/chromedriver.exe'
driver = webdriver.Chrome(path)
driver.get("https://terms.naver.com/medicineSearch.nhn")

# 경로 설정
os.chdir('C:/Users/student/Desktop/박성아/global')

# 데이터 불러오기
medical = pd.read_csv("OpenData_ItemPermitDetail20200208.csv",encoding='cp949')
name = medical['품목명']


# 효능효과 네이버 사전 크롤링
def read_effect(col_):

    content = []

    for i in col_:
        try:
            elem = driver.find_element_by_css_selector("#ip_txt")
            elem.clear()
            elem.send_keys(i)
            driver.find_element_by_css_selector('#nameSearchBtn').click()

            driver.find_element_by_css_selector(
                '#content > div.list_wrap > ul > li > div.info_area > div.subject > strong').click()

            html = driver.page_source

            soup = BeautifulSoup(html, 'html.parser')

            for x in soup.select('#size_ct > p:nth-child(12)'):
                content.append(x.get_text())

            driver.find_element_by_css_selector('#content > div.section_wrap > div.headword_title > p.cite > a').click()

        except:
            content.append('None')

# 데이터 프레임으로 형성
con = pd.DataFrame({'품목명':name,'효능효과':content})
con.to_excel('./con_total/con.xlsx')

#######################################################################################################################
# 효능효과 데이터 불러오기
effect = pd.read_excel("효능효과.xlsx")
total = pd.concat([medical,effect['효능효과']],axis=1)

# 취소상태가 정상인 것만 추출
medical2 = total[total['취소상태']=='정상']
medical3 = medical2[['품목일련번호','효능효과','품목명','저장방법','유효기간']].drop_duplicates()

# 품목명 기준 중복 제거
medical4 = medical3.drop_duplicates(['품목명'])

# 의약품 이미지 데이터 불러오기
egg = pd.read_csv('공공데이터개방_낱알식별목록.csv')
egg_drop = egg.drop_duplicates(['품목명'])

# 이미지가 중복되는 품목의 경우 '본 제품의 모양이 상이할 수 있다는 내용 첨부'
a = pd.DataFrame(egg['품목명'].value_counts())
aaa = a[a['품목명']>1]
aaa = aaa.reset_index()
aaa['비고'] = '본 제품의 모양이 상이할 수 있습니다.'

# 컬럼명 수정
aaa.columns = ['품목명','중복수','비고']
medical_total = pd.merge(egg_drop, aaa, on=['품목명'],how='left')

# 최종 데이터셋 생성
medical_egg = pd.merge(medical_total[['품목일련번호','품목명','분류명','큰제품이미지']],medical4,on=['품목명'],how='right')
medical_egg.to_pickle('최종_rev01.pkl')