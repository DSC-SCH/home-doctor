import pandas as pd
import os

# 경로 설정
os.chdir(r'C:\Users\a0105\Desktop\[GlobalChallenge]data_rev01')

# 의약품 상세정보 및 주성분 결합 파일 불러오기
total = pd.read_csv('최종파일.csv',encoding='cp949')
# 주성분의 na가 있는 경우 제외
total2 = total.dropna(subset=['주성분'])

# 개수 확인
total2.shape   # 50679
total.shape    # 50784

# 인덱스 복구
total2= total2.reset_index()

# 주성분수와 / 개수 다름
# / 외 다른 구분자 확인 필요
total2['check'] = total2['주성분'].str.count('/')+1

# 품목기준코드를 주성분 수만큼 반복 생성
list_ = []
for x in range(len(total2)):
    code = []
    for i in range(total2['check'][x]):
        code.append(total2['품목기준코드'][x])
    list_.append(code)

abc = []
for i in list_:
    for j in i:
        abc.append(j)

# 주성분 문자열 나눔
main_property = total2['주성분'].str.split('/')

result = []
for i in main_property:
    for j in i:
        result.append(j)

len(abc)
len(result)

result_total = pd.DataFrame(data={'품목기준코드':abc,'품명':result},columns=['품목기준코드','품명'])
result_total = result_total.set_index('품명')

# 의약품 부작용 데이터 불러오기
side_effect = pd.read_csv('data/22. 의약품 부작용 정보.csv',encoding='cp949')
# 품명별로 실마리 정보 내용 합치기
side_effect2 = side_effect.groupby('품명')['실마리정보'].agg(lambda col: ', '.join(col)).reset_index()
side_effect2 = side_effect2.set_index('품명')

# 품명을 기준으로 결합
multiple = result_total.join(side_effect2,how='inner')
multiple = multiple.reset_index()

# 품목기준코드 기준으로 품명과 의약품 부작용 정보 그룹화
multiple_total = multiple.groupby('품목기준코드')['품명','실마리정보'].agg(lambda col: ', '.join(col)).reset_index()

# ,,를 ,로 수정
multiple_total['실마리정보'] = multiple_total['실마리정보'].str.replace(',,',',')
