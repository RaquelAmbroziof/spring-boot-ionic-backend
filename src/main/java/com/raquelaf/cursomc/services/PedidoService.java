package com.raquelaf.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raquelaf.cursomc.domain.ItemPedido;
import com.raquelaf.cursomc.domain.PagamentoComBoleto;
import com.raquelaf.cursomc.domain.Pedido;
import com.raquelaf.cursomc.domain.enums.EstadoPagamento;
import com.raquelaf.cursomc.repositories.ItemPedidoRepository;
import com.raquelaf.cursomc.repositories.PagamentoRepository;
import com.raquelaf.cursomc.repositories.PedidoRepository;
import com.raquelaf.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService bolSer;
	
	@Autowired
	private PagamentoRepository pagRepo;
	
	@Autowired
	private ProdutoService prodSer;
	
	@Autowired
	private ItemPedidoRepository itemPedRepo;
	
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			bolSer.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagRepo.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(prodSer.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		itemPedRepo.saveAll(obj.getItens());
		return obj;
	}

}
