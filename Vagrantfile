# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.require_version ">= 1.5.0"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box_url = "http://files.vagrantup.com/precise64.box"
  config.vm.box = "precise64"

  config.vm.network :private_network, type: "dhcp"
  config.vm.network :forwarded_port, guest: 8088, host: 8088
  config.vm.network :forwarded_port, guest: 6379, host: 6379

  config.vm.provider "virtualbox" do |v|
    v.memory = 1024
    v.cpus = 2
  end

  config.vm.provision :ansible do |ansible|
    ansible.playbook = "playbook.yml"
    ansible.verbose ="vvvv"
    # ansible.inventory_path = "ansible/inventories/local"
  end
end

