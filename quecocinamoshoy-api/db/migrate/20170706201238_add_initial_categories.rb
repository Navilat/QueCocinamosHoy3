class AddInitialCategories < ActiveRecord::Migration[5.1]
  def change
  	Category.destroy_all
  	Category.create(name: 'Carnes')
  	Category.create(name: 'Pescados y Mariscos')
  	Category.create(name: 'Ensaladas')
  	Category.create(name: 'Postres')
  	Category.create(name: 'Sopas')
  	Category.create(name: 'Salsas')
  	Category.create(name: 'Bebidas')
  	Category.create(name: 'Tartas y Tortillas')
  end
end
