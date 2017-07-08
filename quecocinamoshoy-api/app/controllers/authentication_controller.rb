class AuthenticationController < ApiController
	before_action :require_login

	# This is protected by API token
	def index
		render json: { prueba: 'Funciona'}
	end
end
