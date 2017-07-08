Rails.application.routes.draw do
	# For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html

	get 'authentication/index'
	get 'categories/index'
	get 'categories/recipes/:id', to: 'categories#recipes'
	get 'recipes/show/:id', to: 'recipes#show'


	scope :format => true, :constraints => { :format => 'json' } do
		post 	"/login" 	=> "sessions#create"
		delete 	"/logout" 	=> "sessions#destroy"
	end

end
