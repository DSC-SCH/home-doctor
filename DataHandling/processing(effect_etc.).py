import pandas as pd
import os
import json

os.chdir(r'C:\Users\student\Desktop\박성아\global')

medical_total = pd.read_pickle('의약품 상세조회(openapi)_수정본.pkl')
medical_total = medical_total[medical_total['상태']=='정상']
medical_total = medical_total.drop_duplicates(['품목명'])

medical_total = medical_total.reset_index()
del medical_total['index']

def processing(col_,d_col):

    col_= col_.str.strip()

    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle&nbsp;title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('\ntitle\ntitle\ntitle\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('\ntitle\ntitle\ntitle', '', regex=True)
    col_=col_.replace('<tbody> \n <tr> \n  <td> <p>-', '', regex=True)
    col_=col_.replace('</p> <p>- 동창</p> </td> \n </tr> \n</tbody>', '', regex=True)
    col_=col_.replace('title\ntitle\ntitle', '', regex=True)
    col_=col_.replace('<sup>', '', regex=True)
    col_=col_.replace('</sup>', '', regex=True)
    col_=col_.replace('<sub>', '', regex=True)
    col_=col_.replace('</sub>', '', regex=True)
    col_=col_.replace(';', '', regex=True)
    col_=col_.replace('&nbsp','',regex=True)
    col_=col_.replace('○','',regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle', 'title\ntitle', regex=True)
    col_=col_.str.split('title\ntitle')

    for x in range(len(col_)):
        if col_[x] == ['']:
            col_[x] = d_col[x]

    return col_

# effect|dosage
medical_total['효능효과_con']=processing(medical_total['효능효과_con'],medical_total['효능효과_tit'])
medical_total['용법용량_con']=processing(medical_total['용법용량_con'],medical_total['용법용량_tit'])

medical_total2= medical_total[['품목기준코드','품목명','저장방법','유효기간','효능효과_con','용법용량_con']]
medical_total2.columns = ['item_code','medicine_name','save_method','valid_dates','effect','dosage']

# side_effect
side = pd.read_csv('의약품 부작용.csv',encoding='cp949')
medical_total3 = pd.merge(medical_total2,side,how='left',left_on=['medicine_name'],right_on=['품목명'])

medical_total3 = medical_total3[['item_code','medicine_name','save_method','valid_dates','effect','dosage','실마리정보(국문)']]
medical_total3.columns=['item_code','medicine_name','save_method','valid_dates','effect','dosage','side_effect']

# image
egg = pd.read_csv('공공데이터개방_낱알식별목록.csv')
egg_drop = egg.drop_duplicates(['품목명'])

# 이미지가 중복되는 품목의 경우 '본 제품의 모양이 상이할 수 있다는 내용 첨부'
image = pd.DataFrame(egg['품목명'].value_counts())
image = image[image['품목명']>1]
image = image.reset_index()
image['비고'] = '본 제품의 모양이 상이할 수 있습니다.'

# 컬럼명 수정
image.columns = ['품목명','중복수','비고']
image_total = pd.merge(egg_drop, image, on=['품목명'],how='left')

# 최종 데이터셋 생성
medical = pd.merge(image_total[['품목일련번호','품목명','큰제품이미지']],medical_total3,left_on=['품목명'],right_on=['medicine_name'],how='right')

del medical['품목일련번호']
del medical['품목명']

medical.columns=['image','item_code','medicine_name','save_method','valid_dates','effect','dosage','bad_effect']
medical = medical[['item_code','medicine_name','effect','save_method','valid_dates','dosage','bad_effect','image']]
medical.shape

# total save
medical.to_json('medical_rev01.json',orient='table')

combination = pd.read_csv('병용금기_완료.csv',encoding='cp949')
del combination['index']

combination.columns = ['combi_name','combi_content','remarks','ban_items']

# combination_ban save
combination.to_json('combination_ban.json',orient='table')
