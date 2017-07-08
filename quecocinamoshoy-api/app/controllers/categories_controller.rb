class CategoriesController < ApiController
	before_action :require_login

	def index
		categorias = Category.all.select(:id, :name)
		render json: {data: categorias, count: categorias.length}, status: :ok
	end

	def recipes
		recetas = Recipe.where(["category_id = ?", params[:id]]).select(:id, :name)
		render json: {data: recetas, count: recetas.length}, status: :ok
	end
end
