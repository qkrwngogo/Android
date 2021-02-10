package com.example.originaltest;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Routine extends Fragment {

    // 설문조사 탭 (나이, 생일, 달력 창)
    TextView AgeView, birthDateView;
    DatePickerDialog datePickerDialog;
    // 회원가입 탭 (약관 동의, 텍스트)
    CheckBox all_agree_box, agree_terms, agree_personal_info;
    TextView email, password, retype_password, name, nick_name, birth_date;

    Button double_check, submit;
    // 로그인 모달 창
    Dialog dialog;

    public static Routine newInstance () {
        return new Routine();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_routine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // 루틴 버튼 클릭 시 로그인 모달창 띄우기
        Button routineBtn = requireView().findViewById(R.id.routine);
        routineBtn.setOnClickListener(v -> isLogined(false));
    }
    // TextView 변경 실시간 화
    public void InitializeView() {
        AgeView = dialog.findViewById(R.id.text_view_age);
        birthDateView = dialog.findViewById(R.id.text_view_birth_date);
    }
    // 달력 클릭 시 TextView에 값 전달
    public DatePickerDialog.OnDateSetListener dataPickerListener = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            AgeView.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
            birth_date.setText((month + 1) + "/" + day + "/" + year);
            birthDateView.setText((month + 1) + "/" + day + "/" + year);
        }
    };
    // 나이 계산 현재 연도 - 탄생 연도 (단, 현재 연도가 탄생 월보다 작을 경우 나이 -1)
    int calculateAge(long date) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)) {
            age --;
        }
        return age;
    }

    /**
     * 로그인 여부 확인
     * @param option : 로그인 여부
     */
    public void isLogined (Boolean option) {
        if(option) {
            //로그인 되어있는 경우
        } else {
            // 로그인 안되어있는 경우
            // 모달창 내부 버튼 설정
            dialog = new Dialog(getContext());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setContentView(R.layout.style_login);
            // 회원 가입 버튼 클릭시 창 이동
            Button signUpBtn = dialog.findViewById(R.id.btn_sign_up);
            signUpBtn.setOnClickListener(v1 -> {
                View signInPage = dialog.findViewById(R.id.sign_in_page);
                signInPage.setVisibility(View.INVISIBLE);
                View signUpPage = dialog.findViewById(R.id.sign_up_page);
                signUpPage.setVisibility(View.VISIBLE);
            });
            // 회원 가입 창 확인식
            password = dialog.findViewById(R.id.password);
            retype_password = dialog.findViewById(R.id.retype_password);
            name = dialog.findViewById(R.id.name);
            nick_name = dialog.findViewById(R.id.nick_name);
            birth_date = dialog.findViewById(R.id.birth_date);
            email = dialog.findViewById(R.id.email);
            double_check = dialog.findViewById(R.id.btn_double_check);
            submit = dialog.findViewById(R.id.submit);

            email.addTextChangedListener(new GenericTextWatcher(email));
            password.addTextChangedListener(new GenericTextWatcher(password));
            name.addTextChangedListener(new GenericTextWatcher(name));
            nick_name.addTextChangedListener(new GenericTextWatcher(nick_name));
            birth_date.addTextChangedListener(new GenericTextWatcher(birth_date));

            // 비밀번호 입력 창을 수정한 뒤 포커스 아웃 할 때 다시 비교
            password.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus && !password.getText().toString().equals("")) {
                    setCorrectStyle(retype_password, password.getText().toString().equals(retype_password.getText().toString()));
                }
            });

            // 비밀번호 재입력이 포커스 아웃 했을 때 비밀번호가 공백이 아닐 경우
            retype_password.setOnFocusChangeListener((v, hasFocus) -> {
                if(!hasFocus && !password.getText().toString().equals("")) {
                    setCorrectStyle(retype_password, password.getText().toString().equals(retype_password.getText().toString()));
                }
            });

            all_agree_box = dialog.findViewById(R.id.agree_all_terms);
            agree_terms = dialog.findViewById(R.id.agree_terms);
            agree_personal_info = dialog.findViewById(R.id.agree_personal_info);
            all_agree_box.setOnClickListener(v -> onCheckChanged((CheckBox)v));
            agree_terms.setOnClickListener(v -> onCheckChanged((CheckBox)v));
            agree_personal_info.setOnClickListener(v -> onCheckChanged((CheckBox)v));
            all_agree_box.setChecked(agree_terms.isChecked() && agree_personal_info.isChecked());

            // 설문조사 탭 (임시 보류)
            InitializeView();
            // birthDate 버튼 클릭 시 달력 호출
            Button birthDateBtn = dialog.findViewById(R.id.btn_birth_date);
            birthDateBtn.setOnClickListener(v2 -> {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // 달력 시작을 현재 날짜로 설정
                datePickerDialog = new DatePickerDialog(getContext(), dataPickerListener, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            });
            // 닫기 버튼 클릭시 화면 닫힘
            ImageView closeBtn = dialog.findViewById(R.id.close);
            closeBtn.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        }
    }


    /**
     * 회원가입 정보 일치 확인 알고리즘
     */

    private class GenericTextWatcher implements TextWatcher {
        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable s) {
            switch(view.getId()) {
                // email이 gmail로 끝나는 지, 또한 6글자 이상인지 확인식
                case R.id.email:
                    discriminant((EditText) email);
                    break;
                // 비밀번호가 6자 이상인지 확인식
                case R.id.password:
                    discriminant((EditText) password);
                    break;
                // 이름이 3글자 이상인지 확인식
                case R.id.name:
                    discriminant((EditText) name);
                    break;
                // 별명이 3글자 이상이지 확인식
                case R.id.nick_name:
                    discriminant((EditText) nick_name);
                    break;
                // 생일이 공백이 아닌지 확인식
                case R.id.birth_date:
                    discriminant((EditText) birth_date);
                    break;
            }
        }
    }

    /**
     *  회원가입 양식 기입 판별식
     */
    @SuppressLint("NonConstantResourceId")
    public void discriminant(EditText editText) {
        // gmail 정규식
        // final String EMAIL_VALIDATION = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@gmail.com";
        final String EMAIL_VALIDATION = "^[a-z]*$";
        // 비밀번호 숫자 문자 특문 2가지 이상 선택 정규식
        final String PASSWORD_VALIDATION = "^" +
                "(?=.*[!@#$%^&+=])" +     // 최소 1개 이상 특수문자
                "(?=\\S+$)" +            // 스페이스 금지
                ".{4,}" +                // 4자 이상
                "$";
        // 이름 정규식, 영어
        final String NAME_VALIDATION = "^[a-zA-Z]*$";
        Pattern pattern;
        Matcher matcher;
        String input = editText.getText().toString().trim();
        // EditText 내용의 길이
        Editable s = editText.getEditableText();
        // EditText 내용
        pattern = Pattern.compile(PASSWORD_VALIDATION);
        matcher = pattern.matcher(input);
        // 확인식
        boolean email_verify, password_verify, name_verify, birth_date_verify;
        // Email 유효성 검사
        email_verify = input.matches(EMAIL_VALIDATION) && s.length() > 5;
        // 비밀 번호 유효성 검사
        password_verify = matcher.matches();
        // 이름 유효성 검사
        name_verify = input.matches(NAME_VALIDATION) && s.length() > 2;
        // 생년월일 유효성 검사
        birth_date_verify = !input.equals("");
        switch (editText.getId()) {
            case R.id.email:
                setCorrectStyle(editText, email_verify);
                break;
            case R.id.password:
                setCorrectStyle(editText, password_verify);
                break;
            case R.id.name:
                setCorrectStyle(editText,name_verify);
                break;
            case R.id.nick_name:
                setCorrectStyle(editText,name_verify);
                if (name_verify) {
                    double_check.setBackgroundResource(R.drawable.custom_button_submit);
                } else {
                    double_check.setBackgroundResource(R.drawable.custom_button_wrong);
                }
                break;
            case R.id.birth_date:
                setCorrectStyle(editText, birth_date_verify);
                break;

        }

    }

    /**
     *
     * @param textView : EditText
     * @param varify : 해당 EditText의 유효성 검사
     */
    public void setCorrectStyle (TextView textView, boolean varify) {
        if (varify) {
            // 조건 충족 시 초록색 밑선
            textView.setBackgroundResource(R.drawable.custom_underline_correct);
            // 공백일 경우 (입력 후 지웠을 경우 포함) 기존 밑선
        } else if(textView.getText().toString().equals("")) {
            textView.setBackgroundResource(R.drawable.custom_underline);
            // 조건 불충족 시 빨간색 밑선
        } else {
            textView.setBackgroundResource(R.drawable.custom_underline_wrong);
        }
    }

    /**
     * 약관 동의 알고리즘
     * @param checkBox : 약관 동의
     */
    private void onCheckChanged(CheckBox checkBox) {
        if (checkBox.getId() == R.id.agree_all_terms) {
            if (all_agree_box.isChecked()) {
                agree_terms.setChecked(true);
                agree_personal_info.setChecked(true);
            } else {
                agree_terms.setChecked(false);
                agree_personal_info.setChecked(false);
            }
        }
        all_agree_box.setChecked(agree_terms.isChecked() && agree_personal_info.isChecked());
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }



}
