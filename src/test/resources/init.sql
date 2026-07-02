update patients set is_active= false;
-- insert into patients (id, first_name, last_name, middle_name, last_name_initial, age_label, birth_date, gender, phone,
--                       oms, snils, relation, oms_attached, attached_clinic_id, is_active)
-- values ('22222222-2222-2222-2222-222222222777','Иван','Иванович','Иванов','В.','33 года','1993-03-05','male','+79031234561','7701000000000023','214-998-477 16','adult',true,'11111111-1111-1111-1111-111111111101',false
--        );
update patients set is_active= true where id = '22222222-2222-2222-2222-222222222777';