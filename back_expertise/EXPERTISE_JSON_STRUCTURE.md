[Назад](README.md)

# Полная структура объекта "Экспертиза" в формате JSON

---

```json
{
  "id": "bb5039c3-d89b-4531-9b51-1d6334719cc2",
  "profileId": "b42b297b-a999-4ac0-909a-059db88164ed",
  "templateName": "b42b297b-a999-4ac0-909a-059db88164ed_26b78a55-16f9-4c35-92dd-9e09f24fac81",
  "caseNumber": "123/2025",
  "speciality": "Строительная экспертиза 1",
  "name": "Экспертиза по гражданскому делу",
  "rulingDate": "2025-01-15",
  "courtName": "Верховный суд Российской Федерации",
  "startDate": "2025-03-01",
  "endDate": "2025-12-31",
  "signDate": "2025-02-27",
  "presidingJudge": "Иванов Иван Иванович",
  "expertiseJudges": [
    {
      "id": "63ce4394-fe05-4c0d-8dcf-71a76aa32237",
      "fullName": "Петрова Анна Сергеевна"
    },
    {
      "id": "88fbadca-1ce5-410c-b6de-2dc82044f99e",
      "fullName": "Кузнецов Сергей Александрович"
    },
    {
      "id": "7117fd10-433e-4a54-afa2-0ceb4635b164",
      "fullName": "Смирнова Елена Викторовна"
    }
  ],
  "plaintiff": "Иванов Алексей Петрович",
  "location": "Москва, Красная площадь, дом 1",
  "volumeCount": "5",
  "participants": "Иванов Алексей Петрович, Попова Мария Ивановна, Сидоров Дмитрий Сергеевич",
  "inspectionDateTime": "2025-03-11T14:30:00",
  "questions": [
    {
      "id": "85f43c11-f9cf-4d56-a73d-22989b5896cd",
      "questionText": "Соответствует ли самовольная постройка строительным нормативам?",
      "answer": "Указанные работы представляют собой перепланировку. В данном случае осуществлялось изменение конфигурации внутренних помещений объекта — демонтаж ненесущих перегородок с целью объединения кухонной зоны и гостиной. Работы не затрагивали несущие конструкции, инженерные коммуникации (водоснабжение, канализацию, отопление или электроснабжение), а также общие параметры здания, такие как высота, этажность или общая площадь. Согласно статье 25 Жилищного кодекса Российской Федерации и пункту 1.7.1 Правил и норм технической эксплуатации жилищного фонда, такие изменения классифицируются исключительно как перепланировка, не переходя в категорию переустройства или реконструкции.",
      "photos": [],
      "answerConclusion": "Самовольная постройка не может быть оценена на соответствие строительным нормативам, так как для этого необходимо наличие разрешительной документации, проекта и акта ввода в эксплуатацию, которые в данном случае отсутствуют.",
      "checklistInstances": [
        {
          "id": "07648fbe-64df-4646-9dbb-de14731d8fa1",
          "checklistId": "550e8400-e29b-41d4-a716-446655440100",
          "checklistName": "Функциональные характеристики объекта строительства",
          "parameterId": "550e8400-e29b-41d4-a716-446655440200",
          "parameterName": "Полы",
          "parameterTypeId": "550e8400-e29b-41d4-a716-446655440400",
          "parameterTypeName": "Ленточный фундамент",
          "subtypeId": "550e8400-e29b-41d4-a716-446655440500",
          "subtypeName": "Бетон",
          "expertiseQuestionId": "85f43c11-f9cf-4d56-a73d-22989b5896cd",
          "createdAt": "2025-04-07T23:06:07.515771",
          "specValues": [
            {
              "id": "a77f3f27-30dc-4798-80a4-4693450be7ba",
              "specId": "550e8400-e29b-41d4-a716-446655440600",
              "specName": "Глубина заложения",
              "value": "30"
            },
            {
              "id": "17314944-cd71-42e8-b913-ef98c603f5e4",
              "specId": "550e8400-e29b-41d4-a716-446655440601",
              "specName": "Ширина",
              "value": "100"
            },
            {
              "id": "8d690097-19bb-470b-9555-c15262477f17",
              "specId": "550e8400-e29b-41d4-a716-446655440602",
              "specName": "Высота",
              "value": "1000"
            }
          ]
        },
        {
          "id": "f2af3c7a-983d-468d-9d56-f8dec9aff449",
          "checklistId": "550e8400-e29b-41d4-a716-446655440100",
          "checklistName": "Функциональные характеристики объекта строительства",
          "parameterId": "550e8400-e29b-41d4-a716-446655440200",
          "parameterName": "Полы",
          "parameterTypeId": "550e8400-e29b-41d4-a716-446655440401",
          "parameterTypeName": "Плиты",
          "subtypeId": null,
          "subtypeName": null,
          "expertiseQuestionId": "85f43c11-f9cf-4d56-a73d-22989b5896cd",
          "createdAt": "2025-04-07T23:35:49.351189",
          "specValues": [
            {
              "id": "a29bfce1-0893-4c45-8a19-84df3bb73693",
              "specId": "550e8400-e29b-41d4-a716-446655440603",
              "specName": "Длина",
              "value": "300"
            },
            {
              "id": "c6b5a503-2364-4225-8aaa-2f4f51eed45d",
              "specId": "550e8400-e29b-41d4-a716-446655440601",
              "specName": "Ширина",
              "value": "1000"
            },
            {
              "id": "3da6c240-26c9-4ceb-836a-e14104f19f59",
              "specId": "550e8400-e29b-41d4-a716-446655440602",
              "specName": "Высота",
              "value": "3000"
            }
          ]
        }
      ]
    },
    {
      "id": "cb199028-7f01-41cd-959a-2dd6e21bc814",
      "questionText": "Являются ли указанные работы реконструкцией или перепланировкой?",
      "answer": null,
      "photos": [],
      "answerConclusion": null,
      "checklistInstances": []
    },
    {
      "id": "051d0f68-354f-4336-b8e5-0e79859ad4fd",
      "questionText": "Создаёт ли постройка угрозу жизни и здоровью граждан?",
      "answer": null,
      "photos": [],
      "answerConclusion": null,
      "checklistInstances": []
    }
  ]
}
```


