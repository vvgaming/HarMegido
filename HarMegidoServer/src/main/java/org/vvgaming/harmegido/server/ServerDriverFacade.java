package org.vvgaming.harmegido.server;

import java.util.List;

import org.unbiquitous.uos.core.UOS;
import org.unbiquitous.uos.core.adaptabitilyEngine.ServiceCallException;
import org.unbiquitous.uos.core.driverManager.DriverData;
import org.unbiquitous.uos.core.driverManager.UosDriver;
import org.unbiquitous.uos.core.messageEngine.dataType.UpDevice;
import org.unbiquitous.uos.core.messageEngine.messages.Call;
import org.unbiquitous.uos.core.messageEngine.messages.Response;

import com.github.detentor.codex.monads.Either;
import com.github.detentor.codex.product.Tuple2;

/**
 * Classe de fachada para o ServerDriver, de modo a simplificar o seu uso. <br/>
 * Todos os métodos possíveis de serem utilizados pelo ServerDriver são métodos
 * desta classe. 
 */
public class ServerDriverFacade 
{
	final UOS uos;
	final ServerDriver serverDriver;

	protected ServerDriverFacade(final UOS uos) {
		super();
		this.uos = uos;
		this.serverDriver = new ServerDriver();
	}

	/**
	 * Cria um facade (fachada) para o driver do servidor a partir do UOS passado como parâmetro.
	 * @param uos A instância do UOS a ser utilizada para a fachada
	 * @return Uma instância da fachada do servidor
	 */
	public static ServerDriverFacade from(final UOS uos) 
	{
		if (uos == null)
		{
			throw new IllegalArgumentException("uos não pode ser nulo");
		}
		if (uos.getGateway() == null)
		{
			throw new IllegalArgumentException("Erro: Gateway do uos não pode retornar null");
		}
		return new ServerDriverFacade(uos);
	}
	
	/**
	 * Retorna um dispositivo a partir do nome do driver. <br/>
	 * @param driver O driver a partir do qual o dispositivo será encontrado
	 * @return Um dispositivo que corresponde ao driver
	 * @throws RuntimeException Se não houver dispositivo corresponde ao driver
	 */
	private UpDevice deviceFromDriver(final UosDriver driver)
	{
		final List<DriverData> listDrivers = uos.getGateway().listDrivers(driver.getDriver().getName());
		
		if (listDrivers == null || listDrivers.isEmpty())
		{
			throw new RuntimeException("Nenhum dispositivo encontrado para o driver passado como parâmetro");
		}
		
		return listDrivers.get(0).getDevice();
	}
	
	/**
	 * Chamada genérica para um serviço deste server. 
	 * @param serviceName O nome do serviço a ser chamado
	 * @param params Os parâmetros a serem repassados para o serviço
	 * @return Uma instância de {@link Either} que irá conter a mensagem de retorno ou a exceção no caso de erro.
	 */
	private Either<ServiceCallException, String> callService(final String serviceName, final Tuple2<String, Object>... params)
	{
		final Call call = new Call(serverDriver.getDriver().getName(), serviceName);
		
		if (params != null)
		{
			for (Tuple2<String, Object> curParam : params)
			{
				call.addParameter(curParam.getVal1(), curParam.getVal2());
			}
		}

		try {
			final Response response = uos.getGateway().callService(deviceFromDriver(serverDriver), call);
			return Either.createRight(response.toString());
		} 
		catch (ServiceCallException e) {
			return Either.createLeft(e);
		}
	}

	/**
	 * Envia uma mensagem para o driver do servidor
	 * @param message A mensagem a ser enviada
	 * @return Uma instância de {@link Either} que irá conter a mensagem de retorno ou a exceção no caso de erro.
	 */
	@SuppressWarnings("unchecked")
	public Either<ServiceCallException, String> sendMessage(final String message) 
	{
		return callService("sendMessage", Tuple2.<String, Object>from("message", message));
	}
}
