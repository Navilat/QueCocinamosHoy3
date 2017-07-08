class AddInitialUsers < ActiveRecord::Migration[5.1]
  def change
  	User.destroy_all
  	User.create(name: 'Marcelo Retamal', email: 'marcelo@gmail.com', password: '123456')
  	User.create(name: 'Nicolás Ávila', email: 'nicolas@gmail.com', password: '123456')
  end
end
