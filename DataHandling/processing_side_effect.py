import os
import pandas as pd
import logging

os.chdir('C:/Users/student/Desktop')

medical = pd.read_csv('./박성아/global/OpenData_ItemPermitDetail20200208.csv',encoding='cp949')
medical = medical[medical['취소상태']=='정상']

medical = medical.drop_duplicates(['품목명'])

medical2 = medical.dropna(subset=['주성분명'])
medical2 = medical2.reset_index()
medical2['주성분명'] = medical2['주성분명'].str.replace('|','/')
medical2['check'] = medical2['주성분명'].str.count('/')+1

list_ = []
for x in range(len(medical2)):
    code = []
    for i in range(medical2['check'][x]):
        code.append(medical2['품목명'][x])
    list_.append(code)

abc = []
for i in list_:
    for j in i:
        abc.append(j)


# 주성분 문자열 나눔
main_property = medical2['주성분명'].str.split('/')

result = []
for i in main_property:
    for j in i:
        result.append(j)

print(len(abc))
print(len(result))

total=[]
for i in result:
    total.append(i[9:])

result_total = pd.DataFrame(data={'품목명':abc,'주성분명':total},columns=['품목명','주성분명'])

side = pd.read_csv('./박성아/global/22. 의약품 부작용 정보.csv',encoding='cp949')
side_effect2 = side.groupby('품명')['실마리정보(국문)'].agg(lambda col: ', '.join(col)).reset_index()
side_effect2.shape

multiple = pd.merge(result_total,side_effect2, right_on='품명', left_on='주성분명')
multiple = multiple.reset_index()

multiple_total = multiple.groupby('품목명')['주성분명','실마리정보(국문)'].agg(lambda col: ', '.join(col)).reset_index()
multiple_total['실마리정보(국문)'] = multiple_total['실마리정보(국문)'].str.replace(',,',',')
multiple_total.to_csv('의약품 부작용.csv',encoding='cp949')


