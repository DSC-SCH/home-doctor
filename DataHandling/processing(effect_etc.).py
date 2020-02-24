import pandas as pd
import os
import json

os.chdir(r'C:\Users\student\Desktop\박성아\global')

medical_total = pd.read_pickle('의약품 상세조회(openapi)_수정본.pkl')
medical_total = medical_total[medical_total['상태']=='정상']
medical_total = medical_total.drop_duplicates(['품목명'])

medical_total = medical_total.reset_index()

del medical_total['index']

medical_total['check']= medical_total['주의사항_con'].str.find('술을 마시는')

medical_total['alchol'] = ''

for x in range(len(medical_total)):
    if medical_total['check'][x]!=-1:
        medical_total['alchol'][x] = '술과 함께 복용불가'
    else:
        medical_total['alchol'][x] = '없음'

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
    col_=col_.replace('&#8226','',regex=True)
    col_=col_.replace('&#x301c','',regex=True)
    col_=col_.replace(['\xad','&nbsp',';','<tbody>','</tbody>','<tr>','</tr>','<p>','</p>','<td rowspan="2">','</td rowspan="2">','<sup>','</sup>','<sub>','</sub>','<td>','</td>','\n'],'',regex=True)    col_=col_.replace(['○','-'],'',regex=True)
    col_=col_.replace('title\ntitle\ntitle\ntitle', 'titletitle', regex=True)
    col_=col_.replace('title\ntitle','titletitle',regex=True)
    col_=col_.str.split('titletitle')

    col_ = col_.replace(' ', '')

    for x in range(len(col_)):
        if col_[x] == ['']:
            col_[x] = d_col[x]

    for a in range(len(col_)):
        col_[a] = [i for i in col_[a] if i]

    return col_

medical_total['효능효과_con']=processing(medical_total['효능효과_con'],medical_total['효능효과_tit'])
medical_total['용법용량_con']=processing(medical_total['용법용량_con'],medical_total['용법용량_tit'])

medical_total2= medical_total[['품목기준코드','품목명','저장방법','유효기간','효능효과_con','용법용량_con','alchol']]
medical_total2.columns = ['item_code','medicine_name','save_method','valid_dates','effect','dosage','alchol']


side = pd.read_csv('의약품 부작용.csv',encoding='cp949')
medical_total3 = pd.merge(medical_total2,side,how='left',left_on=['medicine_name'],right_on=['품목명'])

medical_total3 = medical_total3[['item_code','medicine_name','save_method','valid_dates','effect','dosage','실마리정보(국문)','alchol']]
medical_total3.columns=['item_code','medicine_name','save_method','valid_dates','effect','dosage','bad_effect','alchol']

medical_total3['dosage'] = medical_total3['dosage'].replace(['\n','\\n','/'],'',regex=True)


# 의약품 이미지 데이터 불러오기
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

medical.columns=['image','item_code','medicine_name','save_method','valid_dates','effect','dosage','bad_effect','alchol']
medical = medical[['item_code','medicine_name','effect','save_method','valid_dates','dosage','bad_effect','alchol','image']]
medical.shape

medical['bad_effect'] = medical['bad_effect'].fillna('없음')
medical['image'] = medical['image'].fillna('없음')

medical.to_json('medical_rev01.json',orient='table')
medical[50:100].to_csv('medical_rev01.csv',encoding='utf-8-sig')

combination = pd.read_csv('병용금기_완료.csv',encoding='cp949')
del combination['index']

combination.columns = ['combi_name','combi_content','remarks','ban_items']

combination.to_json('combination_ban.json',orient='table')
