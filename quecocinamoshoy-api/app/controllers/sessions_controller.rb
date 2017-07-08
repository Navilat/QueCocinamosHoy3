class SessionsController < ApiController
	def create
		if user = User.valid_login?(params[:email], params[:password])
			allow_token_to_be_used_only_once_for(user)
			send_auth_token_for_valid_login_of(user)
		else
			render_unauthorized("Error with your login or password")
		end
	end

	def destroy
		logout
		head :ok
	end

	private

	def send_auth_token_for_valid_login_of(user)
		render json: { token: user.token, user_id: user.id, user_name: user.name }
	end

	def allow_token_to_be_used_only_once_for(user)
		user.regenerate_token
	end

	def logout
		current_user.invalidate_token
	end
end
