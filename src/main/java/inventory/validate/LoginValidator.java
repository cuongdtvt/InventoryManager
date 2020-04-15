package inventory.validate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import inventory.model.Users;
import inventory.service.UserService;

@Component
public class LoginValidator implements Validator {
	@Autowired
	private UserService userService;

	/*
	 * Hàm support kiểm tra xem mình sẽ support những class nào khi at truyền vào
	 * đây, ở đây là login validator nên chỉ support lầ class user thôi
	 */
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return clazz == Users.class;
	}

	/* trong validate kiểm tra các thông tin đc gửi từ form lên */
	public void validate(Object target, Errors errors) {
		Users user = (Users) target; // ép kiểu object về user

		// kiểm tra có để trống không
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "msg.required");
		ValidationUtils.rejectIfEmpty(errors, "password", "msg.required");

		// kiểm tra username và pass có đúng hay k?
		if (!StringUtils.isEmpty(user.getUserName()) && !StringUtils.isEmpty(user.getPassword())) {
			List<Users> users = userService.findByProperty("userName", user.getUserName());
			if (user != null && !users.isEmpty()) {
				if (!users.get(0).getPassword().equals(user.getPassword())) {
					errors.rejectValue("password", "msg.wrong.password");
				}
			} else {
				errors.rejectValue("userName", "msg.wrong.username");
			}
		}

	}

}
