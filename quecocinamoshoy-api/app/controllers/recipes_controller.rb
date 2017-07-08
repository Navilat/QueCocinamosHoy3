class RecipesController < ApiController
	before_action :require_login

	def show
		receta = Recipe.select(:name, :user_id, :ingredients, :procedure).find(params[:id])
		usuario = User.select(:name).find(receta.user_id)
		render json: {recipe: receta, user: usuario.name}, status: :ok
	end
end
